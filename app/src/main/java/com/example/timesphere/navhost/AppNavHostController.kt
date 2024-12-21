package com.example.timesphere.navhost

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.screens.HomeScreen
import com.example.timesphere.screens.ProfileScreen
import com.example.timesphere.screens.ReportScreen
import com.example.timesphere.screens.SettingsScreen
import com.example.timesphere.screens.VerifyCredentials
import com.example.timesphere.viewmodels.AppViewModel


sealed class NavDestination(val title: String, val route : String, val icon : ImageVector){
    object Home: NavDestination(title = "Home",route = "home_screen",icon = Icons.Filled.Home)
    object Profile: NavDestination(title = "Profile",route = "profile_screen",icon = Icons.Filled.AccountCircle)
    object Report: NavDestination(title = "Report",route = "report_screen",icon = Icons.Filled.DateRange)
    object Settings: NavDestination(title = "Settings",route = "settings_screen",icon = Icons.Filled.Settings)
}
sealed class AppNavigationDestinations(val title: String, val route : String){
    object Home: NavDestination(title = "Home",route = "home_screen",icon = Icons.Filled.Home)
    object Profile: NavDestination(title = "Profile",route = "profile_screen",icon = Icons.Filled.AccountCircle)
    object Report: NavDestination(title = "Report",route = "report_screen",icon = Icons.Filled.DateRange)
    object Settings: NavDestination(title = "Settings",route = "settings_screen",icon = Icons.Filled.Settings)
    object Credentials: NavDestination(title = "Credentials",route = "verify_credentials_screen",icon = Icons.Filled.Settings)
}


@Composable
fun AppNavHostController(
    appViewModel: AppViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController()){

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
                        selected = currentRoute == destination.route,
                        onClick = {
                            if (currentRoute != destination.route) {
                                navHostController.navigate(destination.route) {
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
                HomeScreen(appViewModel = appViewModel,navHostController = navHostController)
            }
            composable(route = NavDestination.Profile.route) {
                ProfileScreen()
            }
            composable(route = NavDestination.Report.route) {
                ReportScreen(appViewModel = appViewModel)
            }
            composable(route = NavDestination.Settings.route) {
                SettingsScreen()
            }
            composable(route = AppNavigationDestinations.Credentials.route) {
                VerifyCredentials()
            }
            composable(route = NavDestination.Settings.route) {
                SettingsScreen()
            }
            composable(route = NavDestination.Settings.route) {
                SettingsScreen()
            }
        }
    }
}