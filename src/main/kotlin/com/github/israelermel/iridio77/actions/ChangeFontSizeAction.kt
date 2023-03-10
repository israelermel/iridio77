package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbFontSizeEvent
import com.github.israelermel.iridio77.impl.FormActionCommand
import com.github.israelermel.iridio77.ui.FontSizeForm
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class ChangeFontSizeAction : FormActionCommand() {
    override fun adbCommandEvent(project: Project, command: IRNotification) =
        AdbFontSizeEvent(project, command)

    override fun adbForm(project: Project, listener: (Command) -> Unit) =
        FontSizeForm(project, listener)

}