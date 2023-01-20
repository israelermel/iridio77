package com.github.israelermel.iridio77.services

import com.github.israelermel.iridio77.ui.models.DisplayDaltonizerCommand
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@Service
@State(
    name = "DisplayDaltonizerConfiguration", storages = [
        Storage(value = "displayDaltonizerConfiguration.xml")
    ]
)
class DisplayDaltonizerService : PersistentStateComponent<DisplayDaltonizerCommand> {

    private var fontSizeState = DisplayDaltonizerCommand()

    override fun getState(): DisplayDaltonizerCommand = fontSizeState

    override fun loadState(state: DisplayDaltonizerCommand) {
        fontSizeState = state
    }

    companion object {
        fun getInstance(project: Project): DisplayDaltonizerService = project.service()
    }
}