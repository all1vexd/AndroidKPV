package ru.itis.hw_3.domain.model

data class NotificationInfo(
    val channelId: String,
    val originalPriority: NotificationPriority,
    val originalTitle: String,
    val hasReplyAction: Boolean,
    val isExpandable: Boolean,
    val shouldOpenApp: Boolean
)