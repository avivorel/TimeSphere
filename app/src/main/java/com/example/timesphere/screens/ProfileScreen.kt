package com.example.timesphere.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.Modifier
import com.example.timesphere.ui.theme.RoundedImage
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timesphere.viewmodels.AppViewModel


@Composable
fun ProfileScreen(
    appViewModel: AppViewModel = viewModel(),
    navHost: NavHostController = rememberNavController()
) {
    val title by remember { mutableStateOf("Profile screen") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
    ) {
        // Rounded Image
        RoundedImage(
            imageUrl = appViewModel.user.userImage, // Replace with a valid drawable resource
            contentDescription = "Profile Picture",
            cornerRadius = 16
        )

        // Title
        Text(title)
    }
}

