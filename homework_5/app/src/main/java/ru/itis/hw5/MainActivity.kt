package ru.itis.hw5

import MovieScreen
import ProfileScreen
import RegistrationScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ru.itis.hw5.constants.Screen
import ru.itis.hw5.data.database.AppDatabase
import ru.itis.hw5.data.repository.UserRepository
import ru.itis.hw5.presentation.addendum.AddendumScreen
import ru.itis.hw5.presentation.restore.RestoreScreen
import ru.itis.hw5.ui.theme.HW5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this)
        val userRepository = UserRepository(database, this)
        val savedUserId = userRepository.getSession()

        enableEdgeToEdge()
        setContent {
            HW5Theme {

                val navController = rememberNavController()

                val startDest = if (savedUserId != -1) Screen.Main.route else Screen.Login.route

                var currentUserId by remember { mutableStateOf(if (savedUserId != -1) savedUserId else null) }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDest
                    ) {
                        composable(Screen.Login.route) {
                            LoginScreen(
                                onNavigateBackToRegister = {
                                    navController.navigate(Screen.Registration.route)
                                },
                                onLoginSuccess = { user ->
                                    userRepository.saveSession(user.id)
                                    currentUserId = user.id
                                    navController.navigate(Screen.Main.route) {
                                        popUpTo(Screen.Login.route) {
                                            inclusive = true
                                        }
                                    }
                                },
                                onNavigateToRestore = { user ->
                                    navController.navigate(Screen.Restore.createRoute(user.email))
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        composable(Screen.Registration.route) {
                            RegistrationScreen(
                                onNavigateToLogin = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        composable(Screen.Main.route) {
                            MovieScreen(
                                userId = currentUserId ?: 0,
                                onNavigateToAddendum = {
                                    navController.navigate(Screen.Addendum.createRoute())
                                },
                                onNavigateToProfile = {
                                    navController.navigate(Screen.Profile.route)
                                },
                                onMovieClick = { movieId ->
                                    navController.navigate(Screen.Addendum.createRoute(movieId))
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        composable(
                            route = Screen.Addendum.route,
                            arguments = listOf(navArgument("movieId") {
                                type = NavType.IntType
                                defaultValue = -1
                            })
                        ) { backStackEntry ->
                            val movieId = backStackEntry.arguments?.getInt("movieId").takeIf { it != -1 }
                            AddendumScreen(
                                userId = currentUserId ?: 0,
                                movieId = movieId,
                                onNavigateBackToMain = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }

                        composable(Screen.Profile.route) {
                            ProfileScreen(
                                userId = currentUserId ?: 0,
                                onLogout = {
                                    userRepository.clearSession()
                                    currentUserId = null
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(0)
                                    }
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            route = Screen.Restore.route,
                            arguments = listOf(navArgument("email") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val email = backStackEntry.arguments?.getString("email")
                            RestoreScreen(
                                userEmail = email ?: "",
                                onRestoreComplete = {
                                    navController.popBackStack()
                                },
                                onPermanentlyDeleteComplete = {
                                    navController.popBackStack()
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
