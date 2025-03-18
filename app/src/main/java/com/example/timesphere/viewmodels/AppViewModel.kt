package com.example.timesphere.viewmodels

import FirebaseRepository
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesphere.model.User
import com.example.timesphere.model.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

val TAG = "AppViewModel"

class AppViewModel : ViewModel(){
    var user by mutableStateOf(User())
    var firebaseRepository: FirebaseRepository = FirebaseRepository()
    var userPassword by mutableStateOf("")
    var userPasswordRepeated by mutableStateOf("")
    var isLoadingUserOnStartup by mutableStateOf(true)
    var hasError by mutableStateOf(false)
    var clockText by mutableStateOf("Clock In")
    val utils = Utils()
    var isClockedIn by mutableStateOf(false)  // Add this inside AppViewModel


    // Job location coordinates (example: New York City)
    private val JOB_LATITUDE = 32.7757842 // Replace with your job location
    private val JOB_LONGITUDE = 35.0224787 // Replace with your job location
    private val JOB_RADIUS_METERS = 100.0 // 100 meters radius

    // Current user location
    var userLatitude by mutableStateOf(0.0)
    var userLongitude by mutableStateOf(0.0)
    var isLocationPermissionGranted by mutableStateOf(false)
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Check if user is within radius
    fun isWithinJobRadius(): Boolean {
        if (userLatitude == 0.0 || userLongitude == 0.0) return false
        val distance = calculateDistance(userLatitude, userLongitude, JOB_LATITUDE, JOB_LONGITUDE)
        return distance <= JOB_RADIUS_METERS
    }

    // Haversine formula to calculate distance between two points (in meters)
    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000.0 // Earth radius in meters
        val lat1Rad = Math.toRadians(lat1)
        val lat2Rad = Math.toRadians(lat2)
        val deltaLat = Math.toRadians(lat2 - lat1)
        val deltaLon = Math.toRadians(lon2 - lon1)

        val a = sin(deltaLat / 2) * sin(deltaLat / 2) +
                cos(lat1Rad) * cos(lat2Rad) * sin(deltaLon / 2) * sin(deltaLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    fun initializeLocationClient(context: Context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }

    fun fetchUserLocation(context: Context, onLocationFetched: (Boolean) -> Unit) {
        if (!isLocationPermissionGranted) {
            onLocationFetched(false)
            return
        }

        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    userLatitude = location.latitude
                    userLongitude = location.longitude
                    onLocationFetched(true)
                } else {
                    onLocationFetched(false)
                }
            }.addOnFailureListener {
                onLocationFetched(false)
            }
        } catch (e: SecurityException) {
            onLocationFetched(false)
        }
    }

    fun clockInOrOut(context: Context, onResult: (Boolean) -> Unit) {
        fetchUserLocation(context) { locationFetched ->
            if (!locationFetched) {
                onResult(false)
                return@fetchUserLocation
            }

            if (isWithinJobRadius()) {
                firebaseRepository.updateShift(user.inShift) { isSuccessful, isClockedIn ->
                    user.inShift = isClockedIn
                    if (isClockedIn) {
                        clockText = utils.calculateShiftDuration(user.shiftStarted)
                        user.shiftStarted = Timestamp.now()
                    } else {
                        clockText = "Clock In"
                        user.shiftStarted = Timestamp(0, 0)
                    }
                    onResult(isSuccessful)
                }
            } else {
                onResult(false) // User is not within radius
            }
        }
    }
    init {
        Log.d("AppViewModel", "init")
    }

    fun logOut(){
        firebaseRepository.logout()
    }
    fun findEmployeeInEmployer(employerId: String, employeeId: String, email: String) = firebaseRepository.findEmployeeInEmployer(employerId, employeeId, email)

    fun isUserAlreadySignedUp(onResult: (Boolean) -> Unit){
        firebaseRepository.isUserAlreadySignedUp(user,onResult = onResult)
    }
    fun signIn(username:String,password:String,onResult: (Boolean) -> Unit){
        firebaseRepository.signInUser(username,password,onResult)
    }

    fun setUser(querySnapshot: QuerySnapshot) {
        val document = querySnapshot.documents.first()
        user = user.copy(
            uid = "-1", // The UID should remain the same as it is managed by FirebaseAuth
            employerId = document.getString("employerId") ?: user.employerId,
            email = document.getString("email") ?: user.email,
            id = document.getString("id") ?: user.id,
            firstName = document.getString("firstName") ?: user.firstName,
            lastName = document.getString("lastName") ?: user.lastName,
            hourlyPay = document.getDouble("hourlySalary") ?: user.hourlyPay,
            phoneNumber = document.getString("phoneNumber") ?: user.phoneNumber,
            userImage = document.getString("userImage") ?: user.userImage
        )
    }
    fun updateUserAll(employeeData: Any) {

        val res = ((employeeData as Map<*,*>)["employeeData"] as? Map<*, *>) ?: emptyMap<Any, Any>()

        user = user.copy(
            firstName = res["fname"] as? String ?: "Unknown",
            lastName = res["lname"] as? String ?: "Unknown",
            isEmployer = res["isManager"] as? Boolean ?: false,
            employerId = res["businessNumber"] as? String ?: "N/A",
            id = res["id"] as? String ?: "N/A",
            hourlyPay = if((res["hourlyRate"] is Double)) (res["hourlyRate"] as Double) else (res["hourlyRate"] as Int).toDouble() ,
            email = res["email"] as? String ?: "Unknown"
        )

    }
    fun updateUserField(field: String, value: String) {
        user = when (field) {
            "email" -> user.copy(email = value)
            "employerId" -> user.copy(employerId = value)
            "id" -> user.copy(id = value)
            else -> user
        }
    }
    fun checkIfPasswordMatch() : Boolean{
        return userPassword == userPasswordRepeated
    }
    fun signUpUser(onResult: (Boolean) -> Unit) {
        firebaseRepository.signUpUserAndCreateEmployeeDocument(user,userPassword,onResult = onResult)
        isLoadingUserOnStartup = false
    }



    fun fetchUser() {
        firebaseRepository.getUserFromFirestore { user ->
            if(user != null) {
                this.user = user
            }
            else{
                this.user = User()
                hasError = true
            }
            isLoadingUserOnStartup = false
        }
    }

    fun trackShift(){
        viewModelScope.launch {
            while (true) {
                if (user.inShift) {
                    clockText = utils.calculateShiftDuration(user.shiftStarted)
                }
                delay(2500)
            }
        }
    }

//    fun clockInOrOut(onResult: (Boolean) -> Unit){
//        firebaseRepository.updateShift(user.inShift){ isSuccessful, isClockedIn ->
//            user.inShift = isClockedIn
//            if(isClockedIn){
//                clockText = utils.calculateShiftDuration(user.shiftStarted)
//                user.shiftStarted = Timestamp.now()
//            }
//            else{
//                clockText = "Clock In"
//                user.shiftStarted = Timestamp(0,0)
//            }
//            onResult(isSuccessful)
//        }
//    }

//    fun updateUserImage(newImageUrl: String) {
//        user = user.copy(userImage = newImageUrl) // Update the local state
//    }
fun updateUserImage(newImageUrl: String) {
    val uid = user.uid // The unique ID of the current user
    if (uid.isEmpty()) {
        Log.e(TAG, "Failed to update image: User ID is empty")
        return
    }

    // Update the Firestore document for the user
    firebaseRepository.updateUserProfileImageUrl(newImageUrl) { isSuccessful ->
        if (isSuccessful) {
            // Update the local state only after Firestore is successfully updated
            user = user.copy(userImage = newImageUrl)
            Log.d(TAG, "User image updated successfully in Firestore")
        } else {
            Log.e(TAG, "Failed to update user image in Firestore")
        }
    }
}






}