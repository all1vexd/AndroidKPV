package ru.itis.hw_3.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Settings
import ru.itis.hw_3.domain.model.BottomNavItem
import ru.itis.hw_3.domain.model.NavLabel

object BottomNavConstants {
    val items = listOf(
        BottomNavItem(
            label = NavLabel.SETTINGS.text,
            icon = Icons.Filled.Settings,
            route = Screen.Settings.route
        ),
        BottomNavItem(
            label = NavLabel.EDIT.text,
            icon = Icons.Filled.Edit,
            route = Screen.Edit.route
        ),
        BottomNavItem(
            label = NavLabel.MESSAGES.text,
            icon = Icons.Filled.Email,
            route = Screen.Message.route
        )
    )
}