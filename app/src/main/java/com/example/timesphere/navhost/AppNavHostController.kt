package com.example.timesphere.navhost

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

// Navigation destinations
sealed class NavDestination(val title: String, val route: String, val icon: ImageVector) {
    object Home : NavDestination("Home", "home_screen", Icons.Filled.Home)
    object Profile : NavDestination("Profile", "profile_screen", Icons.Filled.AccountCircle)
    object Report : NavDestination("Report", "report_screen", Icons.Filled.DateRange)
    object Settings : NavDestination("Settings", "settings_screen", Icons.Filled.Settings)
}

// Separate sealed class for all app destinations including non-bottom-nav ones
sealed class AppNavigationDestinations(val route: String) {
    object Home : AppNavigationDestinations("home_screen")
    object Profile : AppNavigationDestinations("profile_screen")
    object Report : AppNavigationDestinations("report_screen")
    object Settings : AppNavigationDestinations("settings_screen")
    object Credentials : AppNavigationDestinations("verify_credentials_screen")
}

@Composable
fun AppNavHostController(
    appViewModel: AppViewModel = viewModel(),
    navHostController: NavHostController = rememberNavController()
) {
    val items = listOf(
        NavDestination.Home,
        NavDestination.Profile,
        NavDestination.Report,
        NavDestination.Settings
    )

    BackgroundContainer {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    // Optional: Ensure navigation bar has a semi-transparent background
                    // to let the gradient show through
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.2f))
                ) {
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
                                        // Prevent multiple instances of same screen
                                        popUpTo(navHostController.graph.startDestinationId) {
                                            saveState = true
                                        }
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
                    HomeScreen(appViewModel = appViewModel, navHostController = navHostController)
                }
                composable(route = NavDestination.Profile.route) {
                    ProfileScreen(appViewModel = appViewModel, navHost = navHostController)
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
            }
        }
    }
}

@Composable
fun BackgroundContainer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF6200EE), // Purple
                        Color(0xFF03DAC5)  // Teal
                    )
                )
            )
    ) {
        content()
    }
}