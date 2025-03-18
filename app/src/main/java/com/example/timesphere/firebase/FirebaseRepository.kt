import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.liveData
import com.example.timesphere.model.Shift
import com.example.timesphere.model.User
import com.example.timesphere.model.FunctionResult
import com.example.timesphere.model.Utils
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import java.util.Calendar
//import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage



val TAG = "AuthRepository"

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    val functions = FirebaseFunctions.getInstance()


    // Logout
    fun logout() {
        auth.signOut()
    }

    fun isUserAlreadySignedUp(
        user: User,
        onResult: (Boolean) -> Unit
    ) {
        val data = hashMapOf("userId" to user.id)
        functions
            .getHttpsCallable("isUserAlreadySignedUp")
            .call(data)
            .continueWith { task ->
                task.result?.getData() as Boolean
            }.addOnCompleteListener { task ->
                if(task.isSuccessful){
                    onResult(!task.result)
                }
                else{
                    onResult(true)
                }
            }

    }


    fun checkUserStatus(onResult: (Boolean) -> Unit) {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            checkIfNewUser(currentUser, onResult)
        } else {
            onResult(false)
        }
    }


    private fun checkIfNewUser(user: FirebaseUser, onResult: (Boolean) -> Unit) {
        val creationTime = user.metadata?.creationTimestamp
        val lastSignInTime = user.metadata?.lastSignInTimestamp

        if (creationTime == lastSignInTime) {
            onResult(true)
        } else {
            onResult(false)
        }
    }


    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign-in success, update UI accordingly.
                val user = auth.currentUser
            } else {
                Log.w("SignIn", "signInWithEmail:failure", task.exception)
            }
        }
    }

    private fun signOutUser() {
        auth.signOut()
    }

    fun updateShiftStartTime(shift: Shift,time:Timestamp,isEnd: Boolean, onResult: (Boolean) -> Unit){
        val update = mapOf(
            if(!isEnd)
            "timeStarted" to time
            else
            "timeFinished" to time
        )
        firestore.collection("shifts").document(shift.shiftId)
            .update(update)
            .addOnSuccessListener {
                if(!isEnd) {
                    Log.d(TAG, "updateShiftStartTime: Successfully updated shift time")
                }
                else{
                    Log.d(TAG, "updateShiftEndTime: Successfully updated shift time")
                }
                onResult(true)
            }
            .addOnFailureListener{
                Log.d(TAG, "updateShiftStartTime: Failed to update shift time")
                onResult(false)
            }
    }

    fun findEmployeeInEmployer(employerId: String, employeeId: String, email: String) = liveData {
        emit(FunctionResult.Loading)
        try {
            val data = hashMapOf(
                "businessNumber" to employerId,
                "id" to employeeId,
                "email" to email
            )

            val result = FirebaseFunctions.getInstance()
                .getHttpsCallable("getEmployeeData")
                .call(data)
                .continueWith { task ->
                    task.result?.getData() as? Map<*, *>
                }.await()
            val test = 3
            emit(FunctionResult.Success(result))
        } catch (e: Exception) {
            emit(FunctionResult.Error(e))
        }
    }



    fun signUpUserAndCreateEmployeeDocument(
        user: User,
        password:String,
        onResult: (Boolean) -> Unit
    ) {
                auth
                    .createUserWithEmailAndPassword(user.email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = task.result?.user

                            if (firebaseUser?.uid != null) {
                                // Create a new User object to be saved in Firestore under 'users' collection
                                val newUser = hashMapOf(
                                    "uid" to firebaseUser.uid,
                                    "email" to user.email,
                                    "employerId" to user.employerId,
                                    "employeeId" to user.id,
                                    "firstName" to user.firstName,
                                    "lastName" to user.lastName,
                                    "hourlySalary" to user.hourlyPay,
                                    "phoneNumber" to user.phoneNumber,
                                    "isEmployer" to user.isEmployer
                                )

                                // Save the new user document in 'users' collection
                                firestore.collection("employees").document(firebaseUser.uid)
                                    .set(newUser)
                                    .addOnSuccessListener {
                                        Log.d("SignUp", "New user successfully added to Firestore")
                                        val update = mapOf(
                                            "isRegistered" to true,
                                            "inShift" to false,
                                            "shiftStarted" to Timestamp(0,0)
                                        )
                                        firestore.collection("employees").document(firebaseUser.uid)
                                            .update(update)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    TAG,
                                                    "update isRegistered -- inShift -- shiftStarted"
                                                )
                                                onResult(true)
                                            }.addOnFailureListener{
                                                onResult(false)
                                            }
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("SignUp", "Failed to add new user to Firestore", e)
                                        onResult(false)  // Failed to create user
                                    }

                            } else {
                                Log.w("SignUp", "Error: Firebase user ID is null")
                                onResult(false)  // Firebase Auth returned null user ID
                            }
                        } else {
                            Log.w("SignUp", "Sign-up failed", task.exception?.cause)
                            onResult(false)  // Firebase Authentication sign-up failed
                        }
                    }
        }




    @SuppressLint("DefaultLocale")
    fun getShiftsForMonth(userId: String, year: Int, month: Int, onResult: (List<Shift>?, Boolean) -> Unit) {
        val startDate = "$year-${String.format("%02d", month)}-01"
        val endDate = "$year-${String.format("%02d", month + 1)}-01"

        firestore.collection("users").document(userId).collection("shifts")
            .whereGreaterThanOrEqualTo("date", startDate)
            .whereLessThan("date", endDate)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val shifts = querySnapshot.documents.mapNotNull { it.toObject(Shift::class.java) }
                onResult(shifts, true)
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error fetching shifts", e)
                onResult(null, false)
            }
    }

    fun getUserFromFirestore(onResult: (User?) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(null)

        firestore.collection("employees")
            .whereEqualTo("uid", uid)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    val data = document.data

                    val user = User(
                        uid = data?.get("uid") as? String ?: "-1",
                        email = data?.get("email") as? String ?: "example@gmail.com",
                        firstName = data?.get("firstName") as? String ?: "firstName",
                        lastName = data?.get("lastName") as? String ?: "lastName",
                        isEmployer = data?.get("isEmployer") as? Boolean ?: false,
                        hourlyPay = (data?.get("hourlySalary") as? Number)?.toDouble() ?: 0.00,
                        employerId = data?.get("employerId") as? String ?: "Employer ID",
                        id = data?.get("employeeId") as? String ?: "Israeli issued ID",
                        phoneNumber = data?.get("phoneNumber") as? String ?: "054-1234567",
                        userImage = data?.get("userImage") as? String ?: null,
                        inShift = data?.get("inShift") as? Boolean ?: false,
                        shiftStarted = data?.get("shiftStarted") as? Timestamp ?: Timestamp(0, 0)
                    )
                    onResult(user)
                } else {
                    onResult(null)
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onResult(null)
            }
    }

    // If not sending time, sets to TimeStamp(0,0)
    fun updateShift(inShift:Boolean, onResult: (Boolean,Boolean) -> Unit){ // return isSuccessful, isClockedIn
        val timeNow = Timestamp.now()
        val initialTime = Timestamp(0,0)
        val uid = auth.currentUser?.uid ?: return onResult(false,false)
        if(inShift){ // clock out
            val update = mapOf(
                "inShift" to false,
                "shiftStarted" to initialTime,
            )
            firestore.collection("employees").document(uid)
                .update(update)
                .addOnSuccessListener {
                    Log.d(TAG, "updateShift: Updated the employee document")
                }.addOnFailureListener{
                    Log.d(TAG, "updateShift: couldnt update employee document")

                }
            val shift = mapOf(
                "timeFinished" to timeNow,
            )
            firestore.collection("shifts")
                .whereEqualTo("uid",uid)
                .whereEqualTo("timeFinished", Timestamp(0,0))
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (!querySnapshot.isEmpty) {
                        val document = querySnapshot.documents[0]
                        val documentId = document.id

                        firestore.collection("shifts").document(documentId)
                            .update(shift)
                            .addOnSuccessListener {
                                Log.d(TAG, "Shift updated successfully in shifts collection")
                                onResult(true,false)
                            }
                            .addOnFailureListener { e ->
                                Log.d(TAG,"Error updating shift: ${e.message}")
                                onResult(false,true)
                            }
                    } else {
                        Log.d(TAG,"No matching shift found.")
                        onResult(false,true)
                    }
                }
                .addOnFailureListener { e ->
                    Log.d(TAG,"Error finding shift: ${e.message}")
                    onResult(false,true)
                }
        }
        else{ // for clock in //return isSuccessful, isClockedIn
            val update = mapOf(
                "inShift" to true,
                "shiftStarted" to timeNow
            )
            val shift = mapOf(
                "timeStarted" to timeNow,
                "timeFinished" to initialTime,
                "uid" to uid
            )
            firestore.collection("shifts").document()
                .set(shift)
                .addOnSuccessListener {
                    firestore.collection("employees").document(uid)
                        .update(update)
                        .addOnSuccessListener {
                                Log.d(TAG, "updateShift: Clocked in successfully")
                                onResult(true,true)
                        }.addOnFailureListener{
                            Log.d(TAG, "updateShift: error clocking in -> couldn't update employee in employees")
                            onResult(false,false)
                        }
                }
                .addOnFailureListener{
                    Log.d(TAG, "updateShift: error clocking in -> couldnt set shift")
                    onResult(false,false)
                }

        }
    }

    fun getShiftsForMonth(
        specifiedMonth: Int? = null, // Optional month (0 = January, 11 = December). If null, use current month (0-based).
        onComplete: (List<Shift>) -> Unit
    ) {
        val calendar = Calendar.getInstance()

        // Determine the month and year (both 0-based for month)
        val month = specifiedMonth ?: calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        // Calculate start of month
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)      // 0-based month: 0 = January, 11 = December
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfMonth = Timestamp(calendar.time)

        // Calculate end of month: move to first day of *next* month, then subtract 1 second
        calendar.add(Calendar.MONTH, 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.add(Calendar.SECOND, -1)
        val endOfMonth = Timestamp(calendar.time)

        // Query Firestore for shifts within the date range
        firestore.collection("shifts")
            .whereEqualTo("uid", auth.currentUser!!.uid)
            .whereGreaterThanOrEqualTo("timeStarted", startOfMonth)
            .whereLessThanOrEqualTo("timeStarted", endOfMonth)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val shifts = Utils().processShiftDocuments(querySnapshot)
                Log.d(TAG, "getShiftsForMonth: retrieved ${shifts.size} shifts")
                onComplete(shifts)
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
                onComplete(emptyList())
            }
    }

    fun updateUserProfileImageUrl(newImageUrl: String, onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return onResult(false) // Get the current user's UID

        // Update the Firestore document for the user
        firestore.collection("employees")
            .document(uid)
            .update("userImage", newImageUrl)
            .addOnSuccessListener {
                Log.d(TAG, "User image URL updated successfully in Firestore")
                onResult(true) // Notify success
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to update user image URL in Firestore: ${exception.message}")
                onResult(false) // Notify failure
            }
    }


//    fun uploadProfileImage(imageUri: android.net.Uri, onResult: (String?, Boolean) -> Unit) {
//        val uid = auth.currentUser?.uid ?: return onResult(null, false)
//        val storageRef = firestore.storage.reference.child("profile_pictures/$uid.jpg")
//
//        storageRef.putFile(imageUri)
//            .addOnSuccessListener {
//                storageRef.downloadUrl.addOnSuccessListener { uri ->
//                    // Return the URL of the uploaded image
//                    onResult(uri.toString(), true)
//                }.addOnFailureListener { exception ->
//                    Log.e(TAG, "Failed to retrieve download URL: ${exception.message}")
//                    onResult(null, false)
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.e(TAG, "Failed to upload image: ${exception.message}")
//                onResult(null, false)
//            }
//    }





    // Helper function to process documents and map them to Shift objects

}
