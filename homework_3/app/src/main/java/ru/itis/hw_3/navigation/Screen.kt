package ru.itis.hw_3.navigation

sealed class Screen(val route:String) {
    object Settings : Screen("settings")
    object Edit : Screen("edit")
    object Message : Screen("message")
}