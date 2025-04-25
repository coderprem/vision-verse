package com.example.visionverse.presentation.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.visionverse.MainActivity
import com.example.visionverse.data.local.manga.MangaEntity
import com.example.visionverse.data.local.manga.MangaEntityData
import com.example.visionverse.presentation.screen.home.HomeScreen
import com.example.visionverse.presentation.screen.auth.SignInScreen
import com.example.visionverse.presentation.screen.auth.SignInViewModel
import com.example.visionverse.presentation.screen.auth.SignUpScreen
import com.example.visionverse.presentation.screen.home.MangaDetailScreen
import com.example.visionverse.presentation.viewmodels.SignUpViewModel
import com.example.visionverse.presentation.viewmodels.SplashViewModel
import kotlin.reflect.typeOf

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    signInViewModel: SignInViewModel,
    signUpViewModel: SignUpViewModel,
    splashViewModel: SplashViewModel,
    activity: MainActivity,
    context: Context
) {
    val isLoggedIn by splashViewModel.isLoggedIn.collectAsState()

    val startDestination = if (isLoggedIn) {
        Screens.HomeScreen
    } else {
        Screens.SignInScreen
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<Screens.SignInScreen> {
            SignInScreen(viewModel = signInViewModel, onNavigateToSignUp = {
                navController.navigate(Screens.SignUpScreen)
            }, onSignInSuccess = {
                navController.navigate(Screens.HomeScreen) {
                    popUpTo(Screens.SignInScreen) { inclusive = true }
                }
            })
        }

        composable<Screens.SignUpScreen> {
            SignUpScreen(viewModel = signUpViewModel, onSignUpSuccess = {
                navController.navigate(Screens.HomeScreen) {
                    popUpTo(Screens.SignUpScreen) { inclusive = true }
                }
            })
        }

        composable<Screens.HomeScreen> {
            HomeScreen(
                activity = activity,
                context = context
            ) {
                navController.navigate(Screens.MangaDetailScreen(it))
            }
        }

        composable<Screens.MangaDetailScreen>(
            typeMap = mapOf(typeOf<MangaEntity>() to MangaEntityData)
        ) { backStackEntry ->
            val manga = backStackEntry.toRoute<Screens.MangaDetailScreen>()
            MangaDetailScreen(
                modifier = modifier,
                manga = manga.manga
            ) {
                navController.navigateUp()
            }
        }
    }
}