package ru.itis.hw_3.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.itis.hw_3.domain.service.NotificationService
import ru.itis.hw_3.screens.FirstScreen
import ru.itis.hw_3.screens.SecondScreen
import ru.itis.hw_3.screens.ThirdScreen
import ru.itis.hw_3.viewmodel.ThirdScreenViewModel

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    notificationService: NotificationService,
) {

    NavHost(
        navController = navController,
        startDestination = Screen.Settings.route
    ) {
        composable(route = Screen.Settings.route) {
            FirstScreen(
                modifier = modifier,
                notificationService = notificationService)
        }
        composable(route = Screen.Edit.route) {
            SecondScreen(
                modifier = modifier,
                notificationService = notificationService
            )
        }
        composable(route = Screen.Message.route) {
            ThirdScreen(
                modifier = modifier
            )
        }
    }
}