package com.github.israelermel.iridio77.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.util.ui.FormBuilder
import javax.swing.JComponent
import javax.swing.JPanel

@Suppress("UNCHECKED_CAST")
class ComboBoxDialog<T>(
    private val project: Project,
    private val items: Array<T>,
    private val title: String,
    private val buttonOkText: String,
    private val onItemSelected: (T) -> Unit
) : DialogWrapper(project) {

    private var selectedItem: T? = null
    private val comboBox: ComboBox<T> = ComboBox<T>().apply {
        items.forEach { addItem(it) }
        addActionListener {
            val item = this.selectedItem as? T
            item?.let { selectedItem = it }
        }
    }

    init {
        init()
    }

    override fun doOKAction() {
        selectedItem?.let { onItemSelected(it) }
        super.doOKAction()
    }

    override fun createCenterPanel(): JComponent {
        return FormBuilder.createFormBuilder()
            .addLabeledComponent(title, comboBox)
            .addComponentFillVertically(JPanel(), 0)
            .panel.apply {
                setOKButtonText(buttonOkText)
            }
    }
}