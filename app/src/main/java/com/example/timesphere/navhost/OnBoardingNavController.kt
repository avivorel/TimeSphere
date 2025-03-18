package com.example.timesphere.navhost

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.screens.VerifyCredentials
import com.example.timesphere.screens.WelcomeScreen
import com.example.timesphere.viewmodels.AppViewModel

import com.example.timesphere.screens.LoginScreen
import com.example.timesphere.screens.PreAuthScreen

sealed class OnBoardingDestinations(val title: String, val route: String) {
    object PreAuthScreen : OnBoardingDestinations(title = "PreAuth", route = "pre_auth_screen")
    object WelcomeScreen : OnBoardingDestinations(title = "welcome", route = "welcome_screen")
    object CredentialsScreen : OnBoardingDestinations(title = "Credentials", route = "verify_credentials_screen")
    object LoginScreen : OnBoardingDestinations(title = "Login", route = "login_screen")
}

@Composable
fun OnboardingNavControllerHost(
    navController: NavHostController = rememberNavController(),
    appViewModel: AppViewModel = viewModel()
) {
    NavHost(
        navController = navController,
        startDestination = OnBoardingDestinations.PreAuthScreen.route
    ) {
        // Pre-Auth Screen
        composable(OnBoardingDestinations.PreAuthScreen.route) {
            PreAuthScreen(navHost = navController)
        }

        // Welcome Screen
        composable(OnBoardingDestinations.WelcomeScreen.route) {
            WelcomeScreen(navHost = navController, appViewModel = appViewModel)
        }

        // Credentials Screen
        composable(route = OnBoardingDestinations.CredentialsScreen.route) {
            VerifyCredentials(navHost = navController, appViewModel = appViewModel)
        }

        // Login Screen
        composable(route = OnBoardingDestinations.LoginScreen.route) {
            LoginScreen(navHost = navController, appViewModel = appViewModel)
        }

        // Home Screen
        composable(route = AppNavigationDestinations.Home.route) {
            AppNavHostController(appViewModel = appViewModel)
            BackHandler(true) {
                Log.i("LOG_TAG", "Clicked back")
            }
        }
    }
}