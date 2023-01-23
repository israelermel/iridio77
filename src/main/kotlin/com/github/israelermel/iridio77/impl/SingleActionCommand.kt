package com.github.israelermel.iridio77.impl

import com.github.israelermel.iridio77.events.AdbActionEvent
import com.github.israelermel.iridio77.utils.AdbManagerCommands
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

abstract class SingleActionCommand : AnAction(), BaseSingleAction {
    override fun actionPerformed(event: AnActionEvent) {
        event.project?.let { project ->
            val notification = IridioNotification(project)

            AdbManagerCommands(project, notification).executeEvent { device ->
                adbCommandEvent(project, notification).execute(device)
            }
        }
    }
}

interface BaseSingleAction {
    fun adbCommandEvent(project: Project, notification: IridioNotification): AdbActionEvent
}