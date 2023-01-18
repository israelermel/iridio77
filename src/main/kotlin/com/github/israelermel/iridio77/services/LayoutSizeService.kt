package com.github.israelermel.iridio77.services

import com.github.israelermel.iridio77.ui.models.LayoutSizes
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@Service
@State(
    name = "LayoutSizeConfiguration", storages = [
        Storage(value = "layoutSizeConfiguration.xml")
    ]
)
class LayoutSizeService : PersistentStateComponent<LayoutSizes> {

    private var layoutSizes = LayoutSizes()

    override fun getState(): LayoutSizes = layoutSizes

    override fun loadState(state: LayoutSizes) {
        layoutSizes = state
    }

    companion object {
        fun getInstance(project: Project): LayoutSizeService = project.service()
    }
}