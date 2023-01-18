package com.github.israelermel.iridio77.services

import com.github.israelermel.iridio77.models.FilePropertiesState
import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@Service
@State(
    name = "FilePropertiesConfiguration", storages = [
        Storage(value = "filePropertiesConfiguration.xml")
    ]
)
class FilePropertiesService : PersistentStateComponent<FilePropertiesState> {

    private var filePropertiesState = FilePropertiesState()

    override fun getState(): FilePropertiesState = filePropertiesState

    override fun loadState(state: FilePropertiesState) {
        filePropertiesState = state
    }

    companion object {
        fun getInstance(project: Project): FilePropertiesService = project.service()
    }
}