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

    private var displayDaltonizerCommand = DisplayDaltonizerCommand()

    override fun getState(): DisplayDaltonizerCommand = displayDaltonizerCommand

    override fun loadState(state: DisplayDaltonizerCommand) {
        displayDaltonizerCommand = state
    }
    fun clearData() {
        displayDaltonizerCommand = DisplayDaltonizerCommand()
    }

    companion object {
        fun getInstance(project: Project): DisplayDaltonizerService = project.service()
    }
}