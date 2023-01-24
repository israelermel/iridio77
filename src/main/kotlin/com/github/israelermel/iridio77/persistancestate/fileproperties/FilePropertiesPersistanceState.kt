package com.github.israelermel.iridio77.persistancestate.fileproperties

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@Service
@State(
    name = "FilePropertiesConfiguration", storages = [Storage(value = "filePropertiesConfiguration.xml")]
)
class FilePropertiesPersistanceState : PersistentStateComponent<FilePropertiesState> {

    private var filePropertiesState = FilePropertiesState()

    override fun getState(): FilePropertiesState = filePropertiesState

    override fun loadState(state: FilePropertiesState) {
        filePropertiesState = state
    }

    companion object {
        fun getInstance(project: Project): FilePropertiesPersistanceState = project.service()
    }
}