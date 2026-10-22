package com.chets.mindpulseapp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chets.mindpulseapp.data.Screens

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screens.Splash.route
    ) {
        composable(Screens.Splash.route) {
            SplashScreen(
                onNavigateToOnboarding = {
                    navController.navigate(Screens.Onboarding.route) {
                        popUpTo(Screens.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToMain = {
                    navController.navigate(Screens.MainApp.route) {
                        popUpTo(Screens.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screens.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screens.MainApp.route) {
                        popUpTo(Screens.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screens.MainApp.route) {
            MainView()
        }
    }
}
