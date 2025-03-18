package com.example.timesphere.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.timesphere.navhost.OnBoardingDestinations

@Composable
fun PreAuthScreen(navHost: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navHost.navigate("login_screen") }, // Navigate to LoginScreen
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Login")
        }

        Button(
            onClick = { navHost.navigate(OnBoardingDestinations.WelcomeScreen.route) }, // Navigate to WelcomeScreen
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Sign Up")
        }
    }
}