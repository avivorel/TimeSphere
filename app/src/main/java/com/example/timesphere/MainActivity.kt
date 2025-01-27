package com.example.timesphere

import FirebaseRepository
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.timesphere.navhost.AppNavHostController
import com.example.timesphere.navhost.OnboardingNavControllerHost
import com.example.timesphere.ui.theme.TimeSphereTheme
import com.example.timesphere.viewmodels.AppViewModel
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {

    private lateinit var appViewModel: AppViewModel
    private lateinit var authRepo: FirebaseRepository
    private var isFirstTime = true

    // Permission result launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            // Handle granted or denied permissions
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    Toast.makeText(this, "$permissionName granted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "$permissionName denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase and view models
        FirebaseApp.initializeApp(this)
        appViewModel = AppViewModel()
        authRepo = FirebaseRepository()

        // Enable edge-to-edge UI
        enableEdgeToEdge()

        // Check for permissions
        checkPermissions()

        // Determine user status
        authRepo.checkUserStatus { isUser ->
            isFirstTime = !isUser
            runApp() // Run the app only after determining user status
        }
    }

    private fun runApp() {
        setContent {
            TimeSphereTheme {
                val navController = rememberNavController()
                if (isFirstTime) {
                    // Onboarding navigation
                    OnboardingNavControllerHost(
                        navController = navController,
                        appViewModel = appViewModel
                    )
                } else {
                    // App navigation
                    appViewModel.fetchUser() // Load user data
                    AppNavHostController(appViewModel = appViewModel)
                }
            }
        }
    }

    private fun checkPermissions() {
        val requiredPermissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )

        val deniedPermissions = requiredPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (deniedPermissions.isNotEmpty()) {
            // Show rationale if needed, then request permissions
            requestPermissionLauncher.launch(deniedPermissions.toTypedArray())
        } else {
            Toast.makeText(this, "All permissions already granted", Toast.LENGTH_SHORT).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {}
