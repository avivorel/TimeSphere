import android.util.Log
import com.example.timesphere.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await


class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Register User
    suspend fun registerUser(email: String, password: String, isEmployer: Boolean): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID not found"))

            val user = User(uid, email, firstName = "", isEmployer = isEmployer)
            firestore.collection("users").document(uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Login User
    suspend fun loginUser(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: return Result.failure(Exception("UID not found"))

            val snapshot = firestore.collection("users").document(uid).get().await()
            val user = snapshot.toObject(User::class.java)
                ?: return Result.failure(Exception("User not found in Firestore"))
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Logout
    fun logout() {
        auth.signOut()
    }

    fun checkUserInFirestore(id: String, email: String, employerId: String,employeeId: String, onResult: (Boolean) -> Unit) {
        firestore.collection("users")
            .whereEqualTo("id", id)
            .whereEqualTo("email", email)
            .whereEqualTo("employerId", employerId)
            .whereEqualTo("employeeId", employeeId)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    Log.d("Firestore", "User exists")
                    onResult(true)
                } else {
                    Log.d("Firestore", "User does not exist")
                    onResult(false)
                }
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "Error checking user", e)
                onResult(false)
            }
    }
}
