package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.services.FontSizeService
import com.github.israelermel.iridio77.ui.models.Command
import com.github.israelermel.iridio77.ui.models.FontSizeCommand
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.layout.panel
import org.jetbrains.kotlin.idea.search.ifTrue
import java.awt.Dimension
import javax.swing.JComponent

class FontSizeForm(
    private val project: Project,
    private val listener: (Command) -> Unit
) : DialogWrapper(project) {

    private var fontSizeCombo: ComboBox<FontSizeCommand> = ComboBox<FontSizeCommand>().apply {
        name = "fontSizeCombo"
    }

    private val fontSizes = arrayOf(
        FontSizeCommand(fontSize = 0.85, label = "Small 0.85"),
        FontSizeCommand(fontSize = 1.0, label = "Default 1.0"),
        FontSizeCommand(fontSize = 1.15, label = "Large 1.15"),
        FontSizeCommand(fontSize = 1.30, label = "Largest 1.30"),
        FontSizeCommand(fontSize = 2.0, label = "Xlargest 2.0")
    )


    lateinit var selectedFontSize: FontSizeCommand
    private var state: FontSizeCommand

    init {
        init()

        state = FontSizeService.getInstance(project).state
        populateFontSizes()
    }

    private fun populateFontSizes() {
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
        FontSizeService.getInstance(project).loadState(selectedFontSize)
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent = panel {
        row("Font Size: ") {
            fontSizeCombo(grow)
        }
    }.apply {
        minimumSize = Dimension(500, 200)
        preferredSize = Dimension(500, 200)
    }
}