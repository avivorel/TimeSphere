package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.navhost.OnboardingNavControllerHost
import com.example.timesphere.ui.theme.ClockInButton
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.ui.theme.TimeSphereTheme
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
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Section: Greeting (fixed height)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(175.dp) // Fixed height as provided
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

                // Card Section: Stretches to bottom
                RoundedCornerCardTop(
                    content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ClockInButton(
                                text = appViewModel.clockText
                            ) {
                                appViewModel.clockInOrOut(context) { isSuccessful ->
                                    val message = if (isSuccessful) "Successful" else "Error: Not near job location"
                                    Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(), // Stretches to bottom navigation bar
                    onClick = { /* No-op */ },
                    onDismiss = { /* No-op */ }
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
