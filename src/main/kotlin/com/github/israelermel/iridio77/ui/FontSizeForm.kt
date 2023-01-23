package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.persistancestate.FontSizePersistanceState
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.ui.models.FontSizeCommand
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue
import java.awt.Dimension
import javax.swing.JComponent

class FontSizeForm(
    private val project: Project,
    private val listener: (Command) -> Unit
) : DialogWrapper(project) {

    private lateinit var selectedFontSize: FontSizeCommand
    private lateinit var state: FontSizeCommand

    private var fontSizeCombo: ComboBox<FontSizeCommand> = ComboBox<FontSizeCommand>().apply {
        name = "fontSizeCombo"
        title = IridioBundle.getMessage(
            "msg.adb.tile.dialog.panel.choose",
            IridioBundle.getMessage("msg.adb.title.font.size")
        )
    }

    private val fontSizes = arrayOf(
        FontSizeCommand(fontSize = 0.85, label = "Small 0.85"),
        FontSizeCommand(fontSize = 1.0, label = "Default 1.0"),
        FontSizeCommand(fontSize = 1.15, label = "Large 1.15"),
        FontSizeCommand(fontSize = 1.30, label = "Largest 1.30"),
        FontSizeCommand(fontSize = 2.0, label = "XLargest 2.0")
    )

    init {
        init()
    }

    private fun setupComboBox() {
        state = FontSizePersistanceState.getInstance(project).state

        with(fontSizes) {
            map { fontSizeCombo.addItem(it) }
            isNotEmpty().ifTrue {
                if (state.getLabel().isEmpty()) {
                    fontSizeCombo.selectedIndex = 1
                } else {
                    fontSizeCombo.selectedIndex = fontSizes.indexOf(state)
                }
            }
        }

        fontSizeCombo.addActionListener {
            val fontSize = fontSizeCombo.selectedItem as? FontSizeCommand
            fontSize?.let {
                selectedFontSize = it
            }
        }
    }

    override fun doOKAction() {
        listener.invoke(selectedFontSize)
        FontSizePersistanceState.getInstance(project).loadState(selectedFontSize)
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent {
        setupComboBox()

        val title = IridioBundle.getMessage("msg.adb.title.font.size")

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(title, fontSizeCombo)
            .panel.apply {
                minimumSize = Dimension(400, 100)
                preferredSize = Dimension(400, 100)
            }
    }
}