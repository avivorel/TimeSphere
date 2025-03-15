package com.example.timesphere.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.ui.theme.RoundedCornerCardTop
import com.example.timesphere.ui.theme.RoundedImageWithLocalUpdate
import com.example.timesphere.viewmodels.AppViewModel
import com.example.timesphere.viewmodels.ShiftsViewModel
import com.example.timesphere.model.Utils

@Composable
fun ProfileScreen(
    appViewModel: AppViewModel = viewModel(),
    shiftsViewModel: ShiftsViewModel = viewModel(),
    navHost: NavHostController = rememberNavController()
) {
    val user = appViewModel.user
    var totalHours by remember { mutableDoubleStateOf(0.00) }
    var daysOff by remember { mutableIntStateOf(0) }
    val utils = Utils()

    LaunchedEffect(shiftsViewModel.shifts) {
        totalHours = shiftsViewModel.shifts.sumOf { shift ->
            utils.getHoursInShift(shift)
        }
        daysOff = utils.getDaysOff(totalHours)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Section: Profile Info (fixed height)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = user.phoneNumber,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            RoundedImageWithLocalUpdate(
                imageUrl = user.userImage,
                contentDescription = "Profile Picture",
                cornerRadius = 16,
                onImageSelected = { imageUri ->
                    appViewModel.updateUserImage(imageUri.toString())
                }
            )
        }

        // Spacer to push the card lower
        Spacer(modifier = Modifier.height(24.dp))

        // Card Section: Stretches to bottom
        RoundedCornerCardTop(
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "Work Summary",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Hourly Rate:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "$${user.hourlyPay}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "Days Off:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                "$daysOff",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val statusText = if (user.inShift) "Currently Clocked In" else "Currently Clocked Out"
                        val statusColor = if (user.inShift) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.tertiary
                        Text(
                            text = statusText,
                            color = statusColor,
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (user.inShift) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Time Elapsed: ${appViewModel.clockText}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            onClick = { /* Optional */ }
        )
    }
}