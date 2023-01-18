package com.github.israelermel.iridio77.settings

import com.github.israelermel.iridio77.models.FilePropertiesState
import com.github.israelermel.iridio77.services.FilePropertiesService
import com.github.israelermel.iridio77.utils.IRFileUtils
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.layout.panel
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class FilePropertiesSettings(private val project: Project) : Configurable, DocumentListener {

    private var modified = false

    private val state: FilePropertiesState by lazy { FilePropertiesService.getInstance(project).state }

    private val keyBuildType: JTextField = JTextField()
    private val valueBuildType: JTextField = JTextField()
    private val keySampleBuildFlavor: JTextField = JTextField()
    private val valueSampleBuildFlavor: JTextField = JTextField()
    private val panel: JPanel = panel() {
        row("Chave BUILD_TYPE") { keyBuildType() }
        row("Valor BUILD_TYPE") { valueBuildType() }

        row("Chave SAMPLE_BUILD_FLAVOR") { keySampleBuildFlavor() }
        row("Valor SAMPLE_BUILD_FLAVOR") { valueSampleBuildFlavor() }
    }

    override fun createComponent(): JComponent {
        keyBuildType.apply {
            text = state.keyBuildType
            document.addDocumentListener(this@FilePropertiesSettings)
        }
        valueBuildType.apply {
            text = state.valueBuildType
            document.addDocumentListener(this@FilePropertiesSettings)
        }
        keySampleBuildFlavor.apply {
            text = state.keySampleBuildFlavor
            document.addDocumentListener(this@FilePropertiesSettings)
        }
        valueSampleBuildFlavor.apply {
            text = state.valueSampleBuildFlavor
            document.addDocumentListener(this@FilePropertiesSettings)
        }
        return panel
    }

    override fun isModified(): Boolean = modified

    override fun apply() {
        state.keyBuildType = keyBuildType.text
        state.valueBuildType = valueBuildType.text
        state.keySampleBuildFlavor = keySampleBuildFlavor.text
        state.valueSampleBuildFlavor = valueSampleBuildFlavor.text

        FilePropertiesService.getInstance(project).loadState(state)

        val filePath = "${project.basePath}/local.properties"
        IRFileUtils.addProperty(filePath, state.keyBuildType, state.valueBuildType)
        IRFileUtils.addProperty(filePath, state.keySampleBuildFlavor, state.valueSampleBuildFlavor)
        modified = false
    }

    override fun getDisplayName(): String = "Iridio77 Properties Settings"
    override fun insertUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun removeUpdate(e: DocumentEvent?) {
        modified = true
    }

    override fun changedUpdate(e: DocumentEvent?) {
        modified = true
    }
}