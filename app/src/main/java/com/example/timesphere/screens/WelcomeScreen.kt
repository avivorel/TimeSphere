package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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

val TAG = "WelcomeScreen"

@Composable
fun WelcomeScreen(
    navHost: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    var result by remember { mutableStateOf<FunctionResult<Map<*, *>?>>(FunctionResult.None) }
    var navigatedOut by remember { mutableStateOf(false) }

    when (result) {
        is FunctionResult.Loading -> {
            Toast.makeText(context, "Loading", Toast.LENGTH_LONG).show()
        }
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
        is FunctionResult.Error -> {
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }
        else -> {}
    }

    BackgroundContainer {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp), // Match HomeScreen padding
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Section: App Name & Greeting (fixed height)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp) // Fixed height to match HomeScreen
                    .padding(top = 32.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Welcome to",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "TimeSphere",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Card Section: Stretches to bottom with subtle gradient background
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
                                .padding(32.dp), // Increased padding to match HomeScreen
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "Login",
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 24.dp)
                            )

                            // Employer ID Field
                            TextField(
                                label = { Text("Employer ID") },
                                value = appViewModel.user.employerId,
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = { appViewModel.updateUserField("employerId", it) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            // Email Field
                            TextField(
                                label = { Text("Email") },
                                value = appViewModel.user.email,
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = { appViewModel.updateUserField("email", it) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            // Israeli ID Field
                            TextField(
                                label = { Text("תעודת זהות") },
                                value = appViewModel.user.id,
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = { appViewModel.updateUserField("id", it) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )

                            Spacer(modifier = Modifier.padding(8.dp))

                            // Face ID Checkbox
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Checkbox(checked = false, onCheckedChange = {})
                                Text("Allow FaceID", style = MaterialTheme.typography.bodyLarge)
                            }

                            // Continue Button
                            Button(
                                onClick = {
                                    appViewModel.findEmployeeInEmployer(
                                        employerId = appViewModel.user.employerId,
                                        employeeId = appViewModel.user.id,
                                        email = appViewModel.user.email
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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), // Stretches to bottom, matching HomeScreen
                onClick = {}
            )
        }
    }
}