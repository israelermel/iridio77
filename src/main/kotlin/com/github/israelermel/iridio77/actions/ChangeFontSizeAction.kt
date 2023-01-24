package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbFontSizeEvent
import com.github.israelermel.iridio77.impl.FormActionCommand
import com.github.israelermel.iridio77.ui.FontSizeForm
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project

class ChangeFontSizeAction : FormActionCommand() {
    override fun adbCommandEvent(project: Project, command: IridioNotification) =
        AdbFontSizeEvent(project, command)

    override fun adbForm(project: Project, listener: (Command) -> Unit) =
        FontSizeForm(project, listener)

}