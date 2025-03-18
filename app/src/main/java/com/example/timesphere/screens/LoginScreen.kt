package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.timesphere.viewmodels.AppViewModel

@Composable
fun LoginScreen(navHost: NavHostController, appViewModel: AppViewModel = viewModel()) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(value = password, onValueChange = { password = it }, label = { Text("Password") })

        Button(
            onClick = {
                val trimmed_email = email.trim()
                appViewModel.signIn(trimmed_email, password) { isSuccess ->
                    if (isSuccess) {
                        appViewModel.fetchUser()
                        navHost.navigate("home_screen") {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    } else {
                        val errorMessage = when (isSuccess) {
                            true-> "login login"
//                            is FirebaseAuthException -> isSuccess.message ?: "Login failed"
                            else -> "Login failed"

                        }
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Login")
        }
    }
}