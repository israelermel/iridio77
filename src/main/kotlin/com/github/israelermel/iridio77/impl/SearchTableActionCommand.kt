package com.github.israelermel.iridio77.impl

import com.github.israelermel.iridio77.events.base.AdbSearchActionEvent
import com.github.israelermel.iridio77.ui.formbase.SearchTableForm
import com.github.israelermel.iridio77.ui.models.PackagesModel
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.utils.AdbManagerCommands
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project

abstract class SearchTableActionCommand : AnAction(), BaseSearchAction {

    override fun actionPerformed(event: AnActionEvent) {
        event.getData(PlatformDataKeys.PROJECT)?.let { project ->
            adbForm(project).run {
                addExecuteListener { executeEvent(project, it) }
                addSearchListener { searchEvent(project, it) }
                showForm()
            }
        }
    }

    private fun searchEvent(project: Project, command: Command) {
        val notification = IRNotification(project)
        AdbManagerCommands(project, notification).executeEvent { device ->
            adbCommandEvent(project, notification).search(device, command).also {
                resultItems(it)
            }
        }
    }

    private fun executeEvent(project: Project, command: Command) {
        val notification = IRNotification(project)
        AdbManagerCommands(project, notification).executeEvent { device ->
            adbCommandEvent(project, notification).remove(device, command)
        }
    }

}

interface BaseSearchAction {
    fun adbCommandEvent(project: Project, notification: IRNotification): AdbSearchActionEvent
    fun adbForm(project: Project): SearchTableForm
    fun resultItems(list: List<PackagesModel>)
}