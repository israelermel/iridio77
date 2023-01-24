package com.github.israelermel.iridio77.impl

import com.github.israelermel.iridio77.events.AdbFormActionEvent
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.utils.AdbManagerCommands
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper

abstract class FormActionCommand : AnAction(), BaseFormAction {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            adbForm(project) { selectFormEvent(project, it) }
        }?.also {
            it.show()
        }
    }

    private fun selectFormEvent(project: Project, command: Command) {
        val notification = IridioNotification(project)
        AdbManagerCommands(project, notification).executeEvent { device ->
            adbCommandEvent(project, notification).execute(device,command)
        }

    }
}

interface BaseFormAction {
    fun adbCommandEvent(project: Project, command: IridioNotification): AdbFormActionEvent
    fun adbForm(project: Project, listener: (Command) -> Unit): DialogWrapper
}