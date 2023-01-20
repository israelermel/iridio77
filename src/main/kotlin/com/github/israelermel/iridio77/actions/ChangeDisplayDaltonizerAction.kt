package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.ui.DisplayDaltonizerForm
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.impl.AndroidDebugBridgeManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ChangeDisplayDaltonizerAction : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            DisplayDaltonizerForm(project) { changeDisplayDaltonizer(project, it) }
        }?.also {
            it.show()
        }
    }

    private fun changeDisplayDaltonizer(project: Project, command: Command) {
        AndroidDebugBridgeManager(project).changeDisplayDaltonizer(command)
    }

}