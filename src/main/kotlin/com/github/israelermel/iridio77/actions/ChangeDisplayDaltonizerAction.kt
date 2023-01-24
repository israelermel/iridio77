package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbDisplayDaltonizerEvent
import com.github.israelermel.iridio77.impl.FormActionCommand
import com.github.israelermel.iridio77.ui.DisplayDaltonizerForm
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class ChangeDisplayDaltonizerAction : FormActionCommand() {
    override fun adbCommandEvent(project: Project, command: IRNotification) =
        AdbDisplayDaltonizerEvent(project, command)

    override fun adbForm(project: Project, listener: (Command) -> Unit) =
        DisplayDaltonizerForm(project, listener)

}