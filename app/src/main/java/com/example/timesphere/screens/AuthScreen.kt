import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.timesphere.viewmodels.AuthViewModelFactory


@Composable
fun AuthScreen(authRepository: AuthRepository) {
    val factory = AuthViewModelFactory(authRepository)
    val viewModel: AuthViewModel = androidx.lifecycle.viewmodel.compose.viewModel(factory = factory)

    val authState by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isEmployer by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") })
        Row {
            Checkbox(checked = isEmployer, onCheckedChange = { isEmployer = it })
            Text("Are you an Employer?")
        }
        Button(onClick = { viewModel.register(email, password, isEmployer) }) {
            Text("Register")
        }
        Button(onClick = { viewModel.login(email, password) }) {
            Text("Login")
        }
        when (authState) {
            is AuthState.Idle -> {
            }

            is AuthState.Loading -> {
                AlertDialog(text =
                { Text("Loading...") },
                    onDismissRequest = {  },
                    confirmButton = { })
            }

            is AuthState.Success -> {
                val user = (authState as AuthState.Success).user
                Text("Welcome, ${user?.firstName ?: "User"}")
            }

            is AuthState.Error -> {
                showDialog = true
                val error = (authState as AuthState.Error).message
                if (showDialog) {
                    AlertDialog(text =
                    { Text(error) },
                        onDismissRequest = { showDialog = false},
                        confirmButton = { Text("Try again") },)
                }
            }

            else -> {}
        }
    }
}

@Preview
@Composable
fun test(){
    AuthScreen(authRepository = AuthRepository())
}
