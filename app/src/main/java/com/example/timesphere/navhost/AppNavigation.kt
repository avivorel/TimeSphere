package com.example.timesphere.navhost

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.screens.HomeScreen
import com.example.timesphere.screens.ProfileScreen
import com.example.timesphere.screens.ReportScreen
import com.example.timesphere.screens.SettingsScreen


sealed class NavDestination(val title: String, val route : String, val icon : ImageVector){
    object Home: NavDestination(title = "Home",route = "home_screen",icon = Icons.Filled.Home)
    object Profile: NavDestination(title = "Profile",route = "profile_screen",icon = Icons.Filled.AccountCircle)
    object Report: NavDestination(title = "Report",route = "report_screen",icon = Icons.Filled.DateRange)
    object Settings: NavDestination(title = "Settings",route = "settings_screen",icon = Icons.Filled.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navHostController = rememberNavController()
    val items = listOf(
        NavDestination.Home, NavDestination.Profile, NavDestination.Report, NavDestination.Settings
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                // Track the current route dynamically
                val currentBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentRoute = currentBackStackEntry?.destination?.route

                items.forEach { destination ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.title
                            )
                        },
                        label = { Text(destination.title) },
                        selected = currentRoute == destination.route, // Update based on current route
                        onClick = {
                            if (currentRoute != destination.route) {
                                navHostController.navigate(destination.route) {
                                    // Remove `popUpTo` to maintain back stack
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navHostController,
            startDestination = NavDestination.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = NavDestination.Home.route) {
                HomeScreen()
            }
            composable(route = NavDestination.Profile.route) {
                ProfileScreen()
            }
            composable(route = NavDestination.Report.route) {
                ReportScreen()
            }
            composable(route = NavDestination.Settings.route) {
                SettingsScreen()
            }
        }
    }
}


@Preview
@Composable
fun previewLoggedIn(){
    AppNavigation()
}