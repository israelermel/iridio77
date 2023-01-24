package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbScreenDensityEvent
import com.github.israelermel.iridio77.impl.FormActionCommand
import com.github.israelermel.iridio77.ui.ScreenDensityForm
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class ChangeScreenDensityAction : FormActionCommand() {
    override fun adbCommandEvent(project: Project, command: IRNotification) =
        AdbScreenDensityEvent(project, command)

    override fun adbForm(project: Project, listener: (Command) -> Unit) =
        ScreenDensityForm(project, listener)
}
