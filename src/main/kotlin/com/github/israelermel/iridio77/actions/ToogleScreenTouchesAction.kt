package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbScreenTouchesEvent
import com.github.israelermel.iridio77.impl.SingleActionCommand
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class ToogleScreenTouchesAction : SingleActionCommand() {
    override fun adbCommandEvent(project: Project, notification: IRNotification) =
        AdbScreenTouchesEvent(project, notification)
}