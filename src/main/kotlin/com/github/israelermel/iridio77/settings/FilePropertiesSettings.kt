package com.github.israelermel.iridio77.settings

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.persistancestate.fileproperties.FilePropertiesState
import com.github.israelermel.iridio77.persistancestate.fileproperties.FilePropertiesPersistanceState
import com.github.israelermel.iridio77.utils.IRFileUtils
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import com.intellij.ui.dsl.builder.text
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class FilePropertiesSettings(private val project: Project) : Configurable, DocumentListener {

    private var modified = false

    private val state: FilePropertiesState by lazy { FilePropertiesPersistanceState.getInstance(project).state }

    private lateinit var keyBuildType: JTextField
    private lateinit var valueBuildType: JTextField
    private lateinit var keySampleBuildFlavor: JTextField
    private lateinit var valueSampleBuildFlavor: JTextField

    override fun createComponent(): JComponent {
        return com.intellij.ui.dsl.builder.panel {
            row(IridioBundle.getMessage("settings.properties.key.first")) {
                textField().text(state.keyBuildType).applyToComponent {
                    document.addDocumentListener(this@FilePropertiesSettings)
                }
            }
            row(IridioBundle.getMessage("settings.properties.value.first")) {
                textField().text(state.valueBuildType).applyToComponent {
                    document.addDocumentListener(this@FilePropertiesSettings)
                }
            }

            row(IridioBundle.getMessage("settings.properties.key.second")) {
                textField().text(state.keySampleBuildFlavor).applyToComponent {
                    document.addDocumentListener(this@FilePropertiesSettings)
                }
            }
            row(IridioBundle.getMessage("settings.properties.value.second")) {
                textField().text(state.valueSampleBuildFlavor).applyToComponent {
                    document.addDocumentListener(this@FilePropertiesSettings)
                }
            }
        }
    }

    override fun isModified(): Boolean = modified

    override fun apply() {
        state.keyBuildType = keyBuildType.text
        state.valueBuildType = valueBuildType.text
        state.keySampleBuildFlavor = keySampleBuildFlavor.text
        state.valueSampleBuildFlavor = valueSampleBuildFlavor.text

        FilePropertiesPersistanceState.getInstance(project).loadState(state)

        val filePath = "${project.basePath}/local.properties"
        IRFileUtils.addProperty(filePath, state.keyBuildType, state.valueBuildType)
        IRFileUtils.addProperty(filePath, state.keySampleBuildFlavor, state.valueSampleBuildFlavor)
        modified = false
    }

    override fun getDisplayName(): String = IridioBundle.getMessage("settings.properties.title")

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