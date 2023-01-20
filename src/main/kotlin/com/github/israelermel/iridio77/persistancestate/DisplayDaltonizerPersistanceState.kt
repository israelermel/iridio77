package com.github.israelermel.iridio77.persistancestate

import com.github.israelermel.iridio77.ui.models.DisplayDaltonizerCommand
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service
@State(
    name = "DisplayDaltonizerConfiguration", storages = [
        Storage(value = "displayDaltonizerConfiguration.xml")
    ]
)
class DisplayDaltonizerPersistanceState : PersistentStateComponent<DisplayDaltonizerCommand> {

    private var displayDaltonizerCommand = DisplayDaltonizerCommand()

    override fun getState(): DisplayDaltonizerCommand = displayDaltonizerCommand

    override fun loadState(state: DisplayDaltonizerCommand) {
        displayDaltonizerCommand = state
    }

    fun clearData() {
        displayDaltonizerCommand = DisplayDaltonizerCommand()
    }

    companion object {
        fun getInstance(project: Project): DisplayDaltonizerPersistanceState = project.service()
    }
}