package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.model.FunctionResult
import com.example.timesphere.ui.theme.BackgroundContainer
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.viewmodels.AppViewModel

@Composable
fun WelcomeScreen(
    navHost: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    var result by remember { mutableStateOf<FunctionResult<Map<*, *>?>>(FunctionResult.None) }
    var navigatedOut by remember { mutableStateOf(false) }

    var employerId by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }

    when (result) {
        is FunctionResult.Loading -> Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
        is FunctionResult.Success -> {
            val employeeData = (result as FunctionResult.Success<*>).data
            if (employeeData != null) {
                appViewModel.updateUserAll(employeeData)
                if (!navigatedOut) {
                    navHost.navigate("verify_credentials_screen")
                    navigatedOut = true
                }
            } else {
                Toast.makeText(context, "No employee found", Toast.LENGTH_LONG).show()
            }
        }
        is FunctionResult.Error -> Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        else -> {}
    }

    BackgroundContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Welcome to", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground, textAlign = TextAlign.Center)
                    Text("TimeSphere", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.primary, textAlign = TextAlign.Center)
                }
            }

            RoundedCornerCardTop(
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("Login", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(vertical = 24.dp))

                            TextField(
                                value = employerId,
                                onValueChange = { employerId = it },
                                label = { Text("Employer ID") },
                                placeholder = { if (employerId.isEmpty()) Text("Enter Employer ID") },
                                modifier = Modifier.fillMaxWidth().onFocusChanged { if (it.isFocused) employerId = "" },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                placeholder = { if (email.isEmpty()) Text("Enter Email") },
                                modifier = Modifier.fillMaxWidth().onFocusChanged { if (it.isFocused) email = "" },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            TextField(
                                value = id,
                                onValueChange = { id = it },
                                label = { Text("תעודת זהות") },
                                placeholder = { if (id.isEmpty()) Text("Enter תעודת זהות") },
                                modifier = Modifier.fillMaxWidth().onFocusChanged { if (it.isFocused) id = "" },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 12.dp)) {
                                Checkbox(checked = false, onCheckedChange = {})
                                Text("Allow FaceID", style = MaterialTheme.typography.bodyLarge)
                            }

                            Button(
                                onClick = {
                                    appViewModel.findEmployeeInEmployer(
                                        employerId = employerId,
                                        employeeId = id,
                                        email = email
                                    ).observeForever { searchResult ->
                                        result = searchResult
                                    }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Continue")
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                onClick = {}
            )
        }
    }
}