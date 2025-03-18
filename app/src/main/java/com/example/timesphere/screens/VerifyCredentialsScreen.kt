package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.ui.theme.BackgroundContainer
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.ui.theme.TimeSphereTheme
import com.example.timesphere.viewmodels.AppViewModel
import java.util.regex.Pattern

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
                                                    appViewModel.signUpUser { isSuccess ->
                                                        if(isSuccess) {
                                                            Toast.makeText(
                                                                context,
                                                                "Signed up successfully",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                            navHost.navigate("home_screen")
                                                        }
                                                        else{
                                                            Toast.makeText(
                                                                context,
                                                                "Password requirements are not met",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
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
    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordValid by remember { mutableStateOf(true) }
    val passwordRequirements = remember {
        listOf(
            "^(?=.*[0-9]).*$" to "Digit",
            "^(?=.*[a-z]).*$" to "Lowercase",
            "^(?=.*[A-Z]).*$" to "Uppercase",
            "^(?=.*[@#\$%^&+=!]).*$" to "Special character",
            "^.{8,}\$" to "Minimum 8 characters"
        )
    }

    val requirementMet = remember(value) {
        passwordRequirements.map { (regex, _) ->
            Pattern.compile(regex).matcher(value).matches()
        }
    }

    Column(modifier = modifier.fillMaxWidth()) {
        TextField(
            value = value,
            onValueChange = { newPassword ->
                onValueChange(newPassword)
                isPasswordValid = validatePassword(newPassword)
            },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            enabled = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide Password" else "Show Password"
                    )
                }
            },
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        if (!isPasswordValid) {
            Text(
                text = "⚠ Password requirements not met!",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Column(modifier = Modifier.padding(top = 8.dp)) {
            passwordRequirements.forEachIndexed { index, (_, requirementText) ->
                val color = if (requirementMet[index]) Color.Green else Color.Red
                Text(
                    text = requirementText,
                    style = MaterialTheme.typography.bodySmall,
                    color = color
                )
            }
        }
    }
}

fun validatePassword(password: String): Boolean {
    val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+$).{8,}$"
    val pattern = Pattern.compile(passwordPattern)
    return pattern.matcher(password).matches()
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