import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.timesphere.R
import com.example.timesphere.model.User
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepositoryRepo: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> get() = _authState

    // Handle Registration
    fun register(email: String, password: String, isEmployer: Boolean) {
        if (isValidEmail(email)) {
                viewModelScope.launch {
                    _authState.value = AuthState.Loading
                    val result = authRepositoryRepo.registerUser(email, password, isEmployer)
                    _authState.value = if (result.isSuccess) AuthState.Success(result.getOrNull())
                    else AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
                }
        }
        else{
            AuthState.Error("Wrong email format!")
        }
    }

    // Handle Login
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = authRepositoryRepo.loginUser(email, password)
            _authState.value = if (result.isSuccess) AuthState.Success(result.getOrNull())
            else AuthState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
        }
    }

    // Handle Logout
    fun logout() {
        authRepositoryRepo.logout()
        _authState.value = AuthState.Idle
    }

    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Start Google Sign-In when button is clicked
}

// Authentication State
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val user: User?) : AuthState()
    data class Error(val message: String) : AuthState()
}
