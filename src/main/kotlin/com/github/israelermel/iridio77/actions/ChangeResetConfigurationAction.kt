package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbResetConfigurationEvent
import com.github.israelermel.iridio77.impl.SingleActionCommand
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class ChangeResetConfigurationAction : SingleActionCommand() {
    override fun adbCommandEvent(project: Project, notification: IridioNotification) =
        AdbResetConfigurationEvent(project, notification)
}