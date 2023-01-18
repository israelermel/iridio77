package com.github.israelermel.iridio77.extensions

import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import java.util.*

fun Project.showNotification(msg: String) {
    NotificationGroup(UUID.randomUUID().toString(), NotificationDisplayType.TOOL_WINDOW).createNotification(
        "Iridio77", msg, NotificationType.WARNING, null
    ).notify(this)
}

fun Project.showNotificationExecutando(msg: String) {
    NotificationGroup(UUID.randomUUID().toString(), NotificationDisplayType.TOOL_WINDOW).createNotification(
        "Iridio77", msg, NotificationType.INFORMATION, null
    ).notify(this)
}

fun Project.showNotification(title: String, msg: String) {
    NotificationGroup(UUID.randomUUID().toString(), NotificationDisplayType.STICKY_BALLOON).createNotification(
        title, msg, NotificationType.WARNING, null
    ).notify(this)
}