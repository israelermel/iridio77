package com.github.israelermel.iridio77.extensions

import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project


fun Project.showNotification(
    msg: String,
    title: String? = null
) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup("Custom Notification Group")
        .createNotification(msg, NotificationType.WARNING)
        .setTitle(title ?: "Iridio77")
        .notify(this)
}

fun Project.showNotificationError(
    msg: String,
    title: String? = null
) {
    NotificationGroupManager.getInstance()
        .getNotificationGroup("Custom Notification Group Error")
        .createNotification(msg, NotificationType.ERROR)
        .setTitle(title ?: "Iridio77")
        .notify(this)
}