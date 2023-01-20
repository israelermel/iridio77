package com.github.israelermel.iridio77.persistancestate

import com.github.israelermel.iridio77.ui.models.FontSizeCommand
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service
@State(
    name = "FontSizeConfiguration", storages = [
        Storage(value = "fontSizeConfiguration.xml")
    ]
)
class FontSizePersistanceState : PersistentStateComponent<FontSizeCommand> {

    private var fontSizeState = FontSizeCommand()

    override fun getState(): FontSizeCommand = fontSizeState

    override fun loadState(state: FontSizeCommand) {
        fontSizeState = state
    }

    fun clearData() {
        fontSizeState = FontSizeCommand()
    }

    companion object {
        fun getInstance(project: Project): FontSizePersistanceState = project.service()
    }
}