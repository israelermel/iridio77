package com.github.israelermel.iridio77.utils

import com.github.israelermel.iridio77.extensions.showNotification
import com.github.israelermel.iridio77.extensions.showNotificationError
import com.intellij.openapi.project.Project

class IridioNotification(private val project: Project) {

    private val ADB_TITLE = "ADB Events"

    fun adbNotification(msg: String) {
        project.showNotification(
            msg,
            ADB_TITLE
        )
    }

    fun showAdbNotificationError(property: String) {
        project.showNotificationError(
            IridioMessage.getErrorMessage(property), ADB_TITLE
        )
    }
}