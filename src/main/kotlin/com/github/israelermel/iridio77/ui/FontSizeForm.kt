package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.persistancestate.FontSizePersistanceState
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.ui.models.command.FontSizeCommand
import com.github.israelermel.iridio77.ui.models.command.FontSizeState
import com.github.israelermel.iridio77.utils.IRDimension
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue
import javax.swing.JComponent
import javax.swing.JPanel

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
        FontSizeCommand(state = FontSizeState.SMALL),
        FontSizeCommand(state = FontSizeState.DEFAULT),
        FontSizeCommand(state = FontSizeState.LARGE),
        FontSizeCommand(state = FontSizeState.LARGEST),
        FontSizeCommand(state = FontSizeState.XLARGEST)
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
        val buttonOkText = IridioBundle.getMessage("label.button.change")

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(title, fontSizeCombo)
            .addComponentFillVertically(JPanel(), IRDimension.Spacing.S)
            .panel.apply {
                setOKButtonText(buttonOkText)
            }
    }
}