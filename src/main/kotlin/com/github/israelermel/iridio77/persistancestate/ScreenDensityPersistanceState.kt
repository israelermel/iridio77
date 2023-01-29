package com.github.israelermel.iridio77.persistancestate

import com.github.israelermel.iridio77.ui.models.command.DensityCommand
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service
@State(
    name = "LayoutSizeConfiguration", storages = [
        Storage(value = "layoutSizeConfiguration.xml")
    ]
)
class ScreenDensityPersistanceState : PersistentStateComponent<DensityCommand> {

    private var densityCommand = DensityCommand()

    override fun getState(): DensityCommand = densityCommand

    override fun loadState(state: DensityCommand) {
        densityCommand = state
    }

    fun clearData() {
        densityCommand = DensityCommand()
    }

    companion object {
        fun getInstance(project: Project): ScreenDensityPersistanceState = project.service()
    }
}