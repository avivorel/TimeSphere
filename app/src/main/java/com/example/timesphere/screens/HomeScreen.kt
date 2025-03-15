package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.navhost.OnboardingNavControllerHost
import com.example.timesphere.ui.theme.ClockInButton
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.viewmodels.AppViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.Manifest

@Composable
fun HomeScreen(
    appViewModel: AppViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        appViewModel.isLocationPermissionGranted = isGranted
        if (isGranted) {
            appViewModel.initializeLocationClient(context)
            appViewModel.fetchUserLocation(context) {}
        }
    }

    LaunchedEffect(Unit) {
        if (!appViewModel.isLocationPermissionGranted) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        appViewModel.trackShift()
    }

    if (!appViewModel.hasError) {
        if (appViewModel.isLoadingUserOnStartup) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Section: Greeting (fixed height)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .padding(top = 32.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hi ${appViewModel.user.firstName}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                // Card Section: Stretches to bottom with subtle gradient background
                RoundedCornerCardTop(
                    content = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
//                                .background(
////                                    Brush.verticalGradient(
//////                                        colors = listOf(
//////                                            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
//////                                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
//////                                        )
////                                    )
//                                )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(48.dp), // Increased padding to accommodate larger button
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                ClockInButton(
                                    text = appViewModel.clockText,
                                    onClick = {
                                        appViewModel.clockInOrOut(context) { isSuccessful ->
                                            val message = if (isSuccessful) "Successful" else "Error: Not near job location"
                                            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(bottom = 32.dp) // Increased spacing below the button
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    onClick = { /* No-op */ }
                )
            }
        }
    } else {
        LaunchedEffect(Unit) {
            Toast.makeText(context, "Error fetching user", Toast.LENGTH_LONG).show()
            appViewModel.logOut()
            navHostController.navigate("onboarding")
        }
        OnboardingNavControllerHost(navController = navHostController, appViewModel = appViewModel)
    }
}