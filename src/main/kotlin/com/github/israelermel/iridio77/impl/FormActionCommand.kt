package com.github.israelermel.iridio77.impl

import com.github.israelermel.iridio77.events.base.AdbFormActionEvent
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.AdbManagerCommands
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

abstract class FormActionCommand : AnAction(), BaseFormAction {
    override fun actionPerformed(event: AnActionEvent) {
        event.getData(PlatformDataKeys.PROJECT)?.run {
            adbForm(this) { selectFormEvent(this, it) }
        }?.also {
            it.show()
        }
    }

    private fun selectFormEvent(project: Project, command: Command) {
        val notification = IRNotification(project)
        AdbManagerCommands(project, notification).executeEvent { device ->
            adbCommandEvent(project, notification).execute(device, command)
        }
    }
}

interface BaseFormAction {
    fun adbCommandEvent(project: Project, command: IRNotification): AdbFormActionEvent
    fun adbForm(project: Project, listener: (Command) -> Unit): DialogWrapper
}