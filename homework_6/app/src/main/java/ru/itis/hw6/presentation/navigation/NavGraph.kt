package ru.itis.hw6.presentation.navigation

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.hw6.presentation.screens.infoCardScreen.InfoCardScreen
import ru.itis.hw6.presentation.screens.mainScreen.MainScreen

@Composable
fun NavGraph() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(Screen.MainScreen.route) {
            MainScreen(
                onCardClick = {
                    navController.navigate(Screen.InfoCardScreen.createRoute(it.id))
                }
            )
        }
        composable(Screen.InfoCardScreen.route) {
            val songId = Screen.InfoCardScreen.getSongId(it.arguments)

            InfoCardScreen(
                songId = songId,
                navigateToMainScreen = {
                    navController.popBackStack()
                }
            )

        }
    }
}

sealed class Screen(val route: String) {

    data object MainScreen: Screen("mainScreen")

    data object InfoCardScreen: Screen("infoCardScreen/{id}") {

        fun createRoute(songId: Long): String {
            return "infoCardScreen/$songId"
        }

        fun getSongId(arguments: Bundle?): Long {
            return arguments?.getString("id")?.toLong() ?: 0L
        }

    }
}