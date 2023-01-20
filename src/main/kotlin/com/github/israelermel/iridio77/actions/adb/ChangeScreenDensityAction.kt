package com.github.israelermel.iridio77.actions.adb

import com.github.israelermel.iridio77.ui.ScreenDensityForm
import com.github.israelermel.iridio77.ui.models.LayoutSizes
import com.github.israelermel.iridio77.ui.models.toCommand
import com.github.israelermel.iridio77.utils.AndroidDebugBridgeManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ChangeScreenDensityAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            ScreenDensityForm(project) {
                screenDensityResize(project, it)
            }
        }?.also {
            it.show()
        }
    }

    private fun screenDensityResize(project: Project, layoutSizes: LayoutSizes) {
        AndroidDebugBridgeManager(project).screenDensity(layoutSizes.toCommand())
    }
}
