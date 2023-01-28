package com.github.israelermel.iridio77.actions

import com.github.israelermel.iridio77.events.AdbSearchRemovePackagesEvent
import com.github.israelermel.iridio77.impl.SearchTableActionCommand
import com.github.israelermel.iridio77.ui.RemoveAppsForm
import com.github.israelermel.iridio77.ui.models.PackagesModel
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project

class AdbSearchRemovePackagesAction : SearchTableActionCommand() {
    private lateinit var removeAppsForm: RemoveAppsForm
    override fun adbCommandEvent(project: Project, notification: IRNotification) =
        AdbSearchRemovePackagesEvent(project, notification)

    override fun adbForm(project: Project): RemoveAppsForm {
        removeAppsForm = RemoveAppsForm(project)
        return removeAppsForm
    }

    override fun resultItems(list: List<PackagesModel>) {
        removeAppsForm.updateTable(list)
    }
}


