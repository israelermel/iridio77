package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.events.AdbScreenDensityEvent
import com.github.israelermel.iridio77.persistancestate.ScreenDensityPersistanceState
import com.github.israelermel.iridio77.ui.models.command.DensityCommand
import com.github.israelermel.iridio77.ui.models.command.DensityState
import com.github.israelermel.iridio77.utils.IRDimension
import com.github.israelermel.iridio77.utils.IRNotification
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue
import javax.swing.JComponent
import javax.swing.JPanel


class ScreenDensityForm(
    private val project: Project,
    private val listener: (DensityCommand) -> Unit
) : DialogWrapper(project) {

    private lateinit var state: DensityCommand
    private lateinit var selectedDensity: DensityCommand

    private val notification by lazy { IRNotification(project) }
    private val adbScreenDensity by lazy { AdbScreenDensityEvent(project, notification) }

    private var densityCombo: ComboBox<DensityCommand> = ComboBox<DensityCommand>().apply {
        name = "densityCombo"
        title = IridioBundle.getMessage(
            "msg.adb.tile.dialog.panel.choose",
            IridioBundle.getMessage("msg.adb.title.density")
        )
    }

    private val densities = mutableListOf(
        DensityCommand(state = DensityState.HDPI),
        DensityCommand(state = DensityState.XHDPI),
        DensityCommand(state = DensityState.XXHDPI),
        DensityCommand(state = DensityState.XXXHDPI)
    )

    init {
        init()
    }

    override fun doOKAction() {
        listener.invoke(selectedDensity)
        ScreenDensityPersistanceState.getInstance(project).loadState(selectedDensity)
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent {
        setupComboBox()

        val title = IridioBundle.getMessage("msg.adb.label.density")
        val buttonOkText = IridioBundle.getMessage("label.button.change")

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(title, densityCombo)
            .addComponentFillVertically(JPanel(), IRDimension.Spacing.S)
            .panel.apply {
                setOKButtonText(buttonOkText)
            }
    }

    private fun setupComboBox() {
        state = ScreenDensityPersistanceState.getInstance(project).state

        adbScreenDensity.getDefaultDensity()?.let { defaultDensity ->
            DensityCommand(label = "Default Density - ${defaultDensity}dpi", density = defaultDensity).also {
                densities.add(0, it)
            }

        }

        with(densities) {
            map { densityCombo.addItem(it) }

            isNotEmpty().ifTrue {
                if (state.getLabel().isEmpty()) {
                    densityCombo.selectedIndex = 0
                } else {
                    densityCombo.selectedIndex = densities.indexOf(state)
                }
            }
        }

        densityCombo.addActionListener {
            val density = densityCombo.selectedItem as? DensityCommand?
            density?.let {
                selectedDensity = it
            }
        }
    }
}