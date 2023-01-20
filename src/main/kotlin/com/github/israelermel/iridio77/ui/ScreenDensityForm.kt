package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.models.AdbScreenDensity
import com.github.israelermel.iridio77.persistancestate.LayoutSizePersistanceState
import com.github.israelermel.iridio77.ui.models.LayoutSizes
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
    private val listener: (LayoutSizes) -> Unit
) : DialogWrapper(project) {

    private lateinit var state: LayoutSizes
    private lateinit var selectedLayoutSizes: LayoutSizes

    private val notification by lazy { IridioNotification(project) }
    private val adbScreenDensity by lazy { AdbScreenDensity(project, notification) }

    private var densityCombo: ComboBox<LayoutSizes> = ComboBox<LayoutSizes>().apply {
        name = "densityCombo"
        title = IridioBundle.getMessage(
            "titleDialogPanelChoose",
            IridioBundle.getMessage("titleDensity")
        )
    }

    private val densities = mutableListOf(
        LayoutSizes(label = "hdpi - 240dpi", size = (240 * 1.1).toInt(), index = 1),
        LayoutSizes(label = "xhdpi - 320dpi", size = (320 * 1.1).toInt(), index = 2),
        LayoutSizes(label = "xxhdpi - 480dpi", size = (480 * 1.1).toInt(), index = 3),
        LayoutSizes(label = "xxxhdpi - 640dpi", size = (640 * 1.1).toInt(), index = 4)
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

        val title = IridioBundle.getMessage("titleDensity")

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
            val first = LayoutSizes(label = "Default Density - ${it}dpi", size = it, index = 0)
            densities.add(0, first)
        }

        with(densities) {
            map { densityCombo.addItem(it) }

            isNotEmpty().ifTrue {
                if (state.label.isNullOrEmpty()) {
                    densityCombo.selectedIndex = 0
                } else {
                    densityCombo.selectedIndex = densities.indexOf(state)
                }
            }
        }

        densityCombo.addActionListener {
            val density = densityCombo.selectedItem as? LayoutSizes?
            density?.let {
                selectedLayoutSizes = it
            }
        }
    }
}