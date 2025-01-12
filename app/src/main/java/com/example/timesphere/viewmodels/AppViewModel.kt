package com.example.timesphere.viewmodels

import FirebaseRepository
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesphere.model.User
import com.example.timesphere.model.Utils
import com.google.firebase.Timestamp
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val TAG = "AppViewModel"

class AppViewModel : ViewModel(){
    var user by mutableStateOf(User())
    var firebaseRepository: FirebaseRepository = FirebaseRepository()
    var userPassword by mutableStateOf("Password")
    var userPasswordRepeated by mutableStateOf("Repeat password")
    var isLoadingUserOnStartup by mutableStateOf(true)
    var hasError by mutableStateOf(false)
    var clockText by mutableStateOf("Clock In")
    val utils = Utils()


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

    fun clockInOrOut(onResult: (Boolean) -> Unit){
        firebaseRepository.updateShift(user.inShift){ isSuccessful, isClockedIn ->
            user.inShift = isClockedIn
            if(isClockedIn){
                clockText = utils.calculateShiftDuration(user.shiftStarted)
                user.shiftStarted = Timestamp.now()
            }
            else{
                clockText = "Clock In"
                user.shiftStarted = Timestamp(0,0)
            }
            onResult(isSuccessful)
        }
    }



}