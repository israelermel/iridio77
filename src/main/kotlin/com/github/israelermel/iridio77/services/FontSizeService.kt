package com.github.israelermel.iridio77.services

import com.github.israelermel.iridio77.ui.models.FontSizeCommand
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@Service
@State(
    name = "FontSizeConfiguration", storages = [
        Storage(value = "fontSizeConfiguration.xml")
    ]
)
class FontSizeService : PersistentStateComponent<FontSizeCommand> {

    private var fontSizeState = FontSizeCommand()

    override fun getState(): FontSizeCommand = fontSizeState

    override fun loadState(state: FontSizeCommand) {
        fontSizeState = state
    }

    companion object {
        fun getInstance(project: Project): FontSizeService = project.service()
    }
}