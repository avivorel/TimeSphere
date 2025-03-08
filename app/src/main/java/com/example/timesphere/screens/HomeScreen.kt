package com.example.timesphere.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.navhost.OnboardingNavControllerHost
import com.example.timesphere.ui.theme.ClockInButton
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.viewmodels.AppViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import android.Manifest
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

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

    // Request location permission on startup
    LaunchedEffect(Unit) {
        if (!appViewModel.isLocationPermissionGranted) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        appViewModel.trackShift()
    }

    LaunchedEffect(Unit) {
        appViewModel.trackShift()
    }
    if(!appViewModel.hasError) {
        if (appViewModel.isLoadingUserOnStartup) {
            //Splash
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF5F5F5)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 75.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        "Hi " + appViewModel.user.firstName,
                        fontSize = 40.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .weight(3f)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RoundedCornerCardTop(content = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            ClockInButton(text = appViewModel.clockText) {
                                appViewModel.clockInOrOut(context){ isSuccessful->
                                    if(isSuccessful){
                                        Toast.makeText(context,"Successful",Toast.LENGTH_LONG).show()
                                    }
                                    else{
                                        Toast.makeText(context,"Error not near job location",Toast.LENGTH_LONG).show()
                                    }
                                }
                            }
                        }
                    }, modifier = Modifier.fillMaxSize(), onClick = { }) {
                    }
                }
            }
        }
    }
    else{
        Toast.makeText(context,"HAS ERROR FETCHING USER",Toast.LENGTH_LONG).show()
        appViewModel.logOut()
        OnboardingNavControllerHost()
    }
}

@Preview
@Composable
fun PrevHomeScreen(){
    HomeScreen()
}