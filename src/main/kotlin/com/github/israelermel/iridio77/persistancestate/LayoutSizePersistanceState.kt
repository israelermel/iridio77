package com.github.israelermel.iridio77.persistancestate

import com.github.israelermel.iridio77.ui.models.DensityCommand
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
class LayoutSizePersistanceState : PersistentStateComponent<DensityCommand> {

    private var layoutSizes = DensityCommand()

    override fun getState(): DensityCommand = layoutSizes

    override fun loadState(state: DensityCommand) {
        layoutSizes = state
    }

    fun clearData() {
        layoutSizes = DensityCommand()
    }

    companion object {
        fun getInstance(project: Project): LayoutSizePersistanceState = project.service()
    }
}