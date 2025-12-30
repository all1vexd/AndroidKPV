package ru.itis.hw5.constants

sealed class Screen(val route: String) {

    object Login: Screen("login")
    object Registration: Screen("registration")
    object Main: Screen("main")
    object Profile: Screen("profile")
    object Restore : Screen("restore/{email}") {
        fun createRoute(email: String) = "restore/$email"
    }
    object Addendum : Screen("addendum?movieId={movieId}") {
        fun createRoute(movieId: Int? = null) =
            if (movieId != null) "addendum?movieId=$movieId" else "addendum"
    }

}