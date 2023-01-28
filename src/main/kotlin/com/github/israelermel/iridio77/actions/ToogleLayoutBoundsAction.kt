package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbLayoutBoundsEvent
import com.github.israelermel.iridio77.impl.SingleActionCommand
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class ToogleLayoutBoundsAction : SingleActionCommand() {
    override fun adbCommandEvent(project: Project, notification: IRNotification) =
        AdbLayoutBoundsEvent(project, notification)
}