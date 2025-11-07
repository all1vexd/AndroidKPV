package ru.itis.hw_3.data.repository

import android.app.NotificationManager

class NotificationRepository {

    object NotificationStorage {

        private val createdNotifications = mutableSetOf<Int>()

        fun addNotification(id: Int) {
            createdNotifications.add(id)
        }

        fun containsNotification(id: Int): Boolean {
            return createdNotifications.contains(id)
        }

        fun removeNotification(id: Int) {
            createdNotifications.remove(id)
        }

        fun getAllNotifications(): Set<Int> {
            return createdNotifications.toSet()
        }

        fun clearAll() {
            createdNotifications.clear()
        }

        fun syncWithSystem(notificationManager: NotificationManager) {
            val activeNotifications = notificationManager.activeNotifications
            val activeIds = activeNotifications.map { it.id }.toSet()

            // Удаляем из нашего хранилища те уведомления, которых нет в системе
            createdNotifications.removeAll { id ->
                !activeIds.contains(id)
            }
        }

        fun isNotificationActive(notificationManager: NotificationManager, id: Int): Boolean {
            val activeNotifications = notificationManager.activeNotifications
            return activeNotifications.any { it.id == id }
        }
    }

}