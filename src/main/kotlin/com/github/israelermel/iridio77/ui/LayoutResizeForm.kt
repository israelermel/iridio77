package com.github.israelermel.iridio77.ui

import com.android.ddmlib.MultiLineReceiver
import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.extensions.showNotification
import com.github.israelermel.iridio77.services.LayoutSizeService
import com.github.israelermel.iridio77.ui.models.LayoutSizes
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import org.jetbrains.android.sdk.AndroidSdkUtils
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue
import java.awt.Dimension
import javax.swing.JComponent


class LayoutResizeForm(
    private val project: Project,
    private val listener: (LayoutSizes) -> Unit
) : DialogWrapper(project) {

    private lateinit var state: LayoutSizes
    private lateinit var selectedLayoutSizes: LayoutSizes

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
        LayoutSizeService.getInstance(project).loadState(selectedLayoutSizes)
        super.doOKAction()
    }

    private fun getDefaultDensity(): Int? {
        val devices = project.let { AndroidSdkUtils.getDebugBridge(it)?.devices }

        var defaultDensity: Int? = null

        devices?.takeIf { it.isNotEmpty() }?.forEach { device ->
            device.executeShellCommand("wm density", SingleLineLayoutSizeReceiver { firstLine ->
                defaultDensity = firstLine.split(":")[1].trim().toInt()
            })
        } ?: kotlin.run {
            project.showNotification("${devices?.size} device(s) connected")
        }

        return defaultDensity
    }

    private class SingleLineLayoutSizeReceiver(
        private val processFirstLine: (response: String) -> Unit
    ) : MultiLineReceiver() {
        private var cancelled = false
        override fun isCancelled(): Boolean = cancelled

        override fun processNewLines(lines: Array<out String>?) {
            lines?.getOrNull(0)?.let { firstLine ->
                processFirstLine(firstLine)
                cancelled = true
            }
        }
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
        state = LayoutSizeService.getInstance(project).state

        getDefaultDensity()?.let {
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