package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.ui.theme.BackgroundContainer
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.ui.theme.TimeSphereTheme
import com.example.timesphere.viewmodels.AppViewModel

@Composable
fun VerifyCredentials(
    navHost: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel()
) {
    val headline by remember { mutableStateOf("Verify Credentials") }
//    val headlineSubtext by remember { mutableStateOf(
//        """Those were given to us by your Employer.
//            |Please contact him If you see anything wrong here
//        """.trimMargin()
//    ) }
    val context = LocalContext.current

    TimeSphereTheme {
        BackgroundContainer {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp), // Match HomeScreen padding
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Section: Headline and Subtext (fixed height)
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
                            text = headline,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
//                        Text(
//                            text = headlineSubtext,
//                            style = MaterialTheme.typography.bodyLarge,
//                            color = MaterialTheme.colorScheme.onSurfaceVariant,
//                            textAlign = TextAlign.Center,
//                            modifier = Modifier.padding(top = 8.dp)
//                        )
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
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Login",
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(bottom = 24.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    ReadOnlyTextField(
                                        value = appViewModel.user.firstName,
                                        label = "First Name",
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    ReadOnlyTextField(
                                        value = appViewModel.user.lastName,
                                        label = "Last Name",
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    ReadOnlyTextField(
                                        value = appViewModel.user.id,
                                        label = "ID",
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    ReadOnlyTextField(
                                        value = appViewModel.user.phoneNumber,
                                        label = "Phone Number",
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                ReadOnlyTextField(
                                    value = appViewModel.user.email,
                                    label = "Email",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    PasswordTextField(
                                        value = appViewModel.userPassword,
                                        onValueChange = { appViewModel.userPassword = it },
                                        label = "Password",
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    PasswordTextField(
                                        value = appViewModel.userPasswordRepeated,
                                        onValueChange = { appViewModel.userPasswordRepeated = it },
                                        label = "Repeat Password",
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    ReadOnlyTextField(
                                        value = appViewModel.user.hourlyPay.toString(),
                                        label = "Hourly Pay",
                                        modifier = Modifier.weight(1f)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Checkbox(
                                        checked = true,
                                        onCheckedChange = {},
                                        enabled = false,
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                    Text(
                                        text = "Extra Hours",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Spacer(modifier = Modifier.height(24.dp))
                                VerifyButton(
                                    onClick = {
                                        if (appViewModel.checkIfPasswordMatch()) {
                                            appViewModel.isUserAlreadySignedUp { isSignedUp ->
                                                if (isSignedUp) {
                                                    Toast.makeText(context, "Already signed up", Toast.LENGTH_LONG).show()
                                                } else {
                                                    appViewModel.signUpUser {
                                                        Toast.makeText(context, "Signed up successfully", Toast.LENGTH_LONG).show()
                                                        navHost.navigate("home_screen")
                                                    }
                                                }
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(), // Stretches to bottom, matching HomeScreen
                    onClick = { /* No-op */ }
                )
            }
        }
    }
}

@Composable
fun ReadOnlyTextField(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = modifier,
        enabled = false,
        textStyle = TextStyle(textAlign = TextAlign.Center),
        colors = TextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    )
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .focusable(true)
            .onFocusChanged {
                if (it.isFocused && (value == "Password" || value == "Repeat password")) {
                    onValueChange("")
                }
            },
        textStyle = TextStyle(textAlign = TextAlign.Center),
        enabled = true,
        colors = TextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun VerifyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .shadow(12.dp, CircleShape)
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                    )
                )
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Verify",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )
    }
}