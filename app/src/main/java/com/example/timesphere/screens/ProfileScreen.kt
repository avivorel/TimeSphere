package com.example.timesphere.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.timesphere.ui.theme.RoundedImage
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.ui.theme.RoundedImageWithLocalUpdate
import com.example.timesphere.viewmodels.AppViewModel
import com.example.timesphere.viewmodels.ShiftsViewModel
import com.example.timesphere.model.Shift
import com.example.timesphere.model.Utils

//TODO: MIGHT NEED TO ADD THE SHIFTS TO THE APP NAV THINGIE THAT MOVES BETWEEN SCREENS
@Composable
fun ProfileScreen(
    appViewModel: AppViewModel = viewModel(),
    shiftsViewModel: ShiftsViewModel = viewModel(),
    navHost: NavHostController = rememberNavController()
) {
    val user = appViewModel.user
    var total_hours by remember { mutableDoubleStateOf(0.00) }
    var days_off by remember { mutableIntStateOf(0) }
    val utils = Utils()


    LaunchedEffect(shiftsViewModel.shifts) {
        total_hours = shiftsViewModel.shifts.sumOf { shift ->
            utils.getHoursInShift(shift)
        }
        days_off = utils.getDaysOff(total_hours)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start // Start aligns the content to the left
    ) {
        // Profile Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Space between image and text
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Full Name and Email on the Left
            Column(
                modifier = Modifier.weight(1f), // Use available space for text
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start // Align text to the start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Start // Align text to the start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = TextAlign.Start // Align text to the start
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Profile Picture on the Right
//            RoundedImage(
//                imageUrl = user.userImage,
//                contentDescription = "Profile Picture",
//                cornerRadius = 16,
//                modifier = Modifier.size(120.dp) .padding(top=14.dp)// Adjust size if needed
//            )
            // Profile Picture with Local Update
            RoundedImageWithLocalUpdate(
                imageUrl = user.userImage,
                contentDescription = "Profile Picture",
                cornerRadius = 16,
                onImageSelected = { imageUri ->
                    // Update the user's image in the ViewModel
                    appViewModel.updateUserImage(imageUri.toString())
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Rounded Corner Card (Work Summary + Clock-in Status)
        RoundedCornerCardTop(
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Work Summary Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Work Summary", style = MaterialTheme.typography.titleMedium)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Hourly Rate:", style = MaterialTheme.typography.bodyMedium)
                            Text("$${user.hourlyPay}", style = MaterialTheme.typography.bodyMedium)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Days Off:", style = MaterialTheme.typography.bodyMedium)
                            Text("${days_off}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }

                    // Clock-in Status Section
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val statusText = if (user.inShift) {
                            "Currently Clocked In"
                        } else {
                            "Currently Clocked Out"
                        }
                        val statusColor = if (user.inShift) Color.Green else Color.Red

                        Text(
                            text = statusText,
                            color = statusColor,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (user.inShift) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Time Elapsed: ${appViewModel.clockText}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize(),
            onClick = { /* Optional: Add click handling if necessary */ },
            onDismiss = { }
        )
    }
}
