package com.example.visionverse

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.visionverse.presentation.navigation.NavGraph
import com.example.visionverse.presentation.screen.auth.SignInViewModel
import com.example.visionverse.presentation.viewmodels.SignUpViewModel
import com.example.visionverse.presentation.viewmodels.SplashViewModel
import com.example.visionverse.ui.theme.VisionVerseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            if (!arePermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this, CAMERA_PERMISSION, 100
                )
            }
            val context = this
            val navController = rememberNavController()
            val signInViewModel: SignInViewModel = hiltViewModel()
            val signUpViewModel: SignUpViewModel = hiltViewModel()
            val splashViewModel: SplashViewModel = hiltViewModel()

            VisionVerseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavGraph(
                        modifier = Modifier
                            .padding(innerPadding),
                        activity = this,
                        context = context,
                        navController = navController,
                        signInViewModel = signInViewModel,
                        signUpViewModel = signUpViewModel,
                        splashViewModel = splashViewModel,
                    )
                }
            }
        }
    }

    fun arePermissionsGranted(): Boolean {
        return CAMERA_PERMISSION.all { permission ->
            ContextCompat.checkSelfPermission(
                applicationContext,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        val CAMERA_PERMISSION = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    }
}