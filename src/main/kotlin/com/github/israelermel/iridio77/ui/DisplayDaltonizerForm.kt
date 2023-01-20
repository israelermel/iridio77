package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.services.DisplayDaltonizerService
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.ui.models.DisplayDaltonizerCommand
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue
import java.awt.Dimension
import javax.swing.JComponent

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
                "titleDialogPanelChoose",
                IridioBundle.getMessage("title.dialog.display.daltonizer")
            )
        }

    private val displayDaltonizerList = arrayOf(
        DisplayDaltonizerCommand(code = -1, "Disabled Daltonizer"),
        DisplayDaltonizerCommand(code = 0, "Monochromatic"),
        DisplayDaltonizerCommand(code = 11, "Protanomaly"),
        DisplayDaltonizerCommand(code = 12, "Deuteranomaly"),
        DisplayDaltonizerCommand(code = 13, "Tritanomaly")
    )

    init {
        init()
    }

    override fun doOKAction() {
        listener.invoke(selectDisplayDaltonizer)
        DisplayDaltonizerService.getInstance(project).loadState(selectDisplayDaltonizer)
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent {
        setupComboBox()

        val title = IridioBundle.getMessage("title.display.daltonizer")

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(title, displayDaltonizerCombo)
            .panel.apply {
                minimumSize = Dimension(400, 100)
                preferredSize = Dimension(400, 100)
            }
    }

    private fun setupComboBox() {
        state = DisplayDaltonizerService.getInstance(project).state

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