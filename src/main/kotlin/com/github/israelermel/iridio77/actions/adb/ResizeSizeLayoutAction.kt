package com.github.israelermel.iridio77.actions.adb

import com.github.israelermel.iridio77.ui.LayoutResizeForm
import com.github.israelermel.iridio77.ui.models.LayoutSizes
import com.github.israelermel.iridio77.utils.AndroidDebugBridgeManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

class ResizeSizeLayoutAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            LayoutResizeForm(project) {
                resizeDensity(project, it)
            }
        }?.also {
            it.show()
        }
    }

    private fun resizeDensity(project: Project, layoutSizes: LayoutSizes) {
        AndroidDebugBridgeManager(project).resizeLayoutDensity(layoutSizes)
    }
}
