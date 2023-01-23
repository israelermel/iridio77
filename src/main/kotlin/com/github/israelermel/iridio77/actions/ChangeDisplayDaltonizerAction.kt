package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbDisplayDaltonizerEvent
import com.github.israelermel.iridio77.impl.FormActionCommand
import com.github.israelermel.iridio77.ui.DisplayDaltonizerForm
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class ChangeDisplayDaltonizerAction : FormActionCommand() {
    override fun adbCommandEvent(project: Project, command: IridioNotification) =
        AdbDisplayDaltonizerEvent(project, command)

    override fun adbForm(project: Project, listener: (Command) -> Unit) =
        DisplayDaltonizerForm(project, listener)

}