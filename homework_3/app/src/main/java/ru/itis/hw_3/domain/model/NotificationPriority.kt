package ru.itis.hw_3.domain.model

import android.app.Notification
import android.app.NotificationManager

sealed class NotificationPriority(
    val importance: Int,
    val priority: Int
) {
    object Max : NotificationPriority(
        importance = NotificationManager.IMPORTANCE_HIGH,
        priority = Notification.PRIORITY_HIGH
    )

    object High : NotificationPriority(
        importance = NotificationManager.IMPORTANCE_HIGH,
        priority = Notification.PRIORITY_HIGH
    )

    object Medium : NotificationPriority(
        importance = NotificationManager.IMPORTANCE_DEFAULT,
        priority = Notification.PRIORITY_DEFAULT
    )

    object Low : NotificationPriority(
        importance = NotificationManager.IMPORTANCE_LOW,
        priority = Notification.PRIORITY_LOW
    )
}