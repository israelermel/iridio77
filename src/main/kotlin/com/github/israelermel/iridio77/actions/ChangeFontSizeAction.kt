package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.ui.FontSizeForm
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.impl.AndroidDebugBridgeManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ChangeFontSizeAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            FontSizeForm(project) { changeFontSize(project, it) }
        }?.also {
            it.show()
        }
    }

    private fun changeFontSize(project: Project, command: Command) {
        AndroidDebugBridgeManager(project).changeFontSize(command)
    }

}