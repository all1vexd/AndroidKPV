package ru.itis.hw_3.navigation

import android.app.NotificationManager
import android.content.Context
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.itis.hw_3.domain.channel.NotificationChannelManager
import ru.itis.hw_3.domain.service.NotificationService
import ru.itis.hw_3.viewmodel.SharedViewModel

@Composable
fun BottomNavigation(
    sharedViewModel: SharedViewModel
) {
    val navController = rememberNavController()

    val context = LocalContext.current
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelManager = NotificationChannelManager(context, notificationManager)
    val notificationService = NotificationService(context, notificationManager, channelManager)

    LaunchedEffect(Unit) {
        channelManager.createAllChannels()
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                BottomNavConstants.items.forEach { item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = {
                            Text(
                                text = item.label
                            )
                        },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavGraph(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            notificationService = notificationService,
            sharedViewModel = sharedViewModel
        )
    }
}