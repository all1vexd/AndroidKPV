package ru.itis.hw_3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings

object BottomNavConstants {
    val items = listOf(
        BottomNavItem(
            label = "Settings",
            icon = Icons.Filled.Settings,
            route = Screen.Settings.route
        ),
        BottomNavItem(
            label = "Edit",
            icon = Icons.Filled.Edit,
            route = Screen.Edit.route
        ),
        BottomNavItem(
            label = "Messages",
            icon = Icons.Filled.Email,
            route = Screen.Message.route
        )
    )
}