package com.github.israelermel.iridio77.actions.adb

import com.github.israelermel.iridio77.ui.FontSizeForm
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.utils.AndroidDebugBridgeManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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