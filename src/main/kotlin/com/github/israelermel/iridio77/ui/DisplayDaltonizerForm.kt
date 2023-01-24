package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.persistancestate.DisplayDaltonizerPersistanceState
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.ui.models.command.DisplayDaltonizerCommand
import com.github.israelermel.iridio77.ui.models.command.DisplayDaltonizerState
import com.github.israelermel.iridio77.utils.IRDimension
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue
import javax.swing.JComponent
import javax.swing.JPanel

class DisplayDaltonizerForm(
    private val project: Project,
    private val listener: (Command) -> Unit
) : DialogWrapper(project) {

    private lateinit var selectDisplayDaltonizer: DisplayDaltonizerCommand
    private lateinit var state: DisplayDaltonizerCommand

    private var displayDaltonizerCombo: ComboBox<DisplayDaltonizerCommand> =
        ComboBox<DisplayDaltonizerCommand>().apply {
            name = "displayDaltonizerCombo"
            title = IridioBundle.getMessage(
                "msg.adb.tile.dialog.panel.choose",
                IridioBundle.getMessage("title.dialog.display.daltonizer")
            )
            alignmentX = JComponent.CENTER_ALIGNMENT
        }

    private val displayDaltonizerList = arrayOf(
        DisplayDaltonizerCommand(state = DisplayDaltonizerState.DISABLED),
        DisplayDaltonizerCommand(state = DisplayDaltonizerState.MONOCHROMATIC),
        DisplayDaltonizerCommand(state = DisplayDaltonizerState.PROTANOMALY),
        DisplayDaltonizerCommand(state = DisplayDaltonizerState.DEUTERANOMALY),
        DisplayDaltonizerCommand(state = DisplayDaltonizerState.TRITANOMALY)
    )

    init {
        init()
    }

    override fun doOKAction() {
        listener.invoke(selectDisplayDaltonizer)
        DisplayDaltonizerPersistanceState.getInstance(project).loadState(selectDisplayDaltonizer)
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent {
        setupComboBox()

        val title = IridioBundle.getMessage("title.display.daltonizer")
        val buttonOkText = IridioBundle.getMessage("label.button.change")

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(title, displayDaltonizerCombo, IRDimension.Spacing.M, false)
            .addComponentFillVertically(JPanel(), IRDimension.Spacing.S)
            .panel.apply {
                setOKButtonText(buttonOkText)
            }
    }

    private fun setupComboBox() {
        state = DisplayDaltonizerPersistanceState.getInstance(project).state

        with(displayDaltonizerList) {
            map { displayDaltonizerCombo.addItem(it) }
            isNotEmpty().ifTrue {
                if (state.getLabel().isEmpty()) {
                    displayDaltonizerCombo.selectedIndex = 0
                } else {
                    displayDaltonizerCombo.selectedIndex = displayDaltonizerList.indexOf(state)
                }
            }
        }

        displayDaltonizerCombo.addActionListener {
            val fontSize = displayDaltonizerCombo.selectedItem as? DisplayDaltonizerCommand
            fontSize?.let {
                selectDisplayDaltonizer = it
            }
        }
    }
}