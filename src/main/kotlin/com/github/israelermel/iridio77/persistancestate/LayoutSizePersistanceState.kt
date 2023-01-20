package com.github.israelermel.iridio77.persistancestate

import com.github.israelermel.iridio77.ui.models.LayoutSizes
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
class LayoutSizePersistanceState : PersistentStateComponent<LayoutSizes> {

    private var layoutSizes = LayoutSizes()

    override fun getState(): LayoutSizes = layoutSizes

    override fun loadState(state: LayoutSizes) {
        layoutSizes = state
    }

    fun clearData() {
        layoutSizes = LayoutSizes()
    }

    companion object {
        fun getInstance(project: Project): LayoutSizePersistanceState = project.service()
    }
}