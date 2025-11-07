package ru.itis.hw_3.data.repository

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
    }



}