package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.events.AdbScreenDensityEvent
import com.github.israelermel.iridio77.persistancestate.LayoutSizePersistanceState
import com.github.israelermel.iridio77.ui.models.DensityCommand
import com.github.israelermel.iridio77.utils.IridioNotification
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue
import java.awt.Dimension
import javax.swing.JComponent


class ScreenDensityForm(
    private val project: Project,
    private val listener: (DensityCommand) -> Unit
) : DialogWrapper(project) {

    private lateinit var state: DensityCommand
    private lateinit var selectedLayoutSizes: DensityCommand

    private val notification by lazy { IridioNotification(project) }
    private val adbScreenDensity by lazy { AdbScreenDensityEvent(project, notification) }

    private var densityCombo: ComboBox<DensityCommand> = ComboBox<DensityCommand>().apply {
        name = "densityCombo"
        title = IridioBundle.getMessage(
            "msg.adb.tile.dialog.panel.choose",
            IridioBundle.getMessage("msg.adb.title.density")
        )
    }

    private val densities = mutableListOf(
        DensityCommand(label = "hdpi - 240dpi", density = (240 * 1.1).toInt()),
        DensityCommand(label = "xhdpi - 320dpi", density = (320 * 1.1).toInt()),
        DensityCommand(label = "xxhdpi - 480dpi", density = (480 * 1.1).toInt()),
        DensityCommand(label = "xxxhdpi - 640dpi", density = (640 * 1.1).toInt())
    )

    init {
        init()
    }

    override fun doOKAction() {
        listener.invoke(selectedLayoutSizes)
        LayoutSizePersistanceState.getInstance(project).loadState(selectedLayoutSizes)
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent {
        setupComboBox()

        val title = IridioBundle.getMessage("msg.adb.title.font.size")

        return FormBuilder.createFormBuilder()
            .addLabeledComponent(title, densityCombo)
            .panel.apply {
                minimumSize = Dimension(400, 100)
                preferredSize = Dimension(400, 100)
            }
    }

    private fun setupComboBox() {
        state = LayoutSizePersistanceState.getInstance(project).state

        adbScreenDensity.getDefaultDensity()?.let {
            val first = DensityCommand(label = "Default Density - ${it}dpi", density = it)
            densities.add(0, first)
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
                selectedLayoutSizes = it
            }
        }
    }
}