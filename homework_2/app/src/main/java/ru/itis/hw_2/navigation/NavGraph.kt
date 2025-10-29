package ru.itis.hw_2.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.itis.hw_2.screens.FirstScreen
import ru.itis.hw_2.screens.SecondScreen
import ru.itis.hw_2.screens.ThirdScreen
import ru.itis.hw_2.viewModels.ThemeViewModel

@Composable
fun NavGraph(
    modifier: Modifier,
    themeViewModel: ThemeViewModel
) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = NavParams.FIRST_SCREEN, builder = {
        composable(route = NavParams.FIRST_SCREEN) {
            FirstScreen(
                navController = navController
            )
        }
        composable(route = NavParams.secondScreenWithEmail) { entry ->
            val email = entry.arguments?.getString(NavParams.EMAIL_PARAM) ?: ""
            SecondScreen(
                navController = navController,
                email = email,
                themeViewModel = themeViewModel
            )
        }
        composable(route = NavParams.THIRD_SCREEN) {
            ThirdScreen(
                navController = navController
            )
        }

    })
}