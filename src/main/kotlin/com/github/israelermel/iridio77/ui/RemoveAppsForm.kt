package com.github.israelermel.iridio77.ui

import com.github.israelermel.iridio77.IridioBundle
import com.github.israelermel.iridio77.components.HintTextField
import com.github.israelermel.iridio77.extensions.setupPreferredSizeLarge
import com.github.israelermel.iridio77.listeners.IRKeyListener
import com.github.israelermel.iridio77.ui.formbase.SearchTableForm
import com.github.israelermel.iridio77.ui.models.COLUMN_CHECKBOX
import com.github.israelermel.iridio77.ui.models.PackagesModel
import com.github.israelermel.iridio77.ui.models.PackagesTableModel
import com.github.israelermel.iridio77.ui.models.command.Command
import com.github.israelermel.iridio77.ui.models.command.SearchRemovePackagesCommand
import com.github.israelermel.iridio77.utils.IRDimension
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.BooleanTableCellEditor
import com.intellij.ui.BooleanTableCellRenderer
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import com.intellij.util.ui.FormBuilder
import org.jetbrains.kotlin.utils.addToStdlib.ifTrue
import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.event.KeyEvent
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.ListSelectionModel

class RemoveAppsForm(
    project: Project
) : DialogWrapper(project), SearchTableForm {

    private lateinit var scrollTablePanel: JBScrollPane
    private lateinit var searchedData: List<PackagesModel>
    private lateinit var searchButton: JButton
    private lateinit var packagesTextFied: HintTextField
    private lateinit var panelSearchFields: JPanel
    private lateinit var jPanel: JPanel
    private var searchTable: JBTable? = null
    private var removeListener: (Command) -> Unit? = {}
    private var searchListener: (Command) -> Unit? = {}

    init {
        init()
        isOKActionEnabled = false
    }

    override fun addExecuteListener(onClickListener: (Command) -> Unit) {
        removeListener = onClickListener
    }

    override fun addSearchListener(onClickListener: (Command) -> Unit) {
        searchListener = onClickListener
    }

    override fun createCenterPanel(): JComponent {
        title = getMessage("msg.adb.title.search.remove.packages")

        createSearchPackageName()
        setupSearchTable()

        val packagesLabel = JBLabel(getMessage("msg.adb.label.search.textfield"))
        val labelListOfApps = JBLabel(getMessage("msg.adb.label.table.list.founded"))

        jPanel = FormBuilder.createFormBuilder()
            .addComponent(packagesLabel, IRDimension.Spacing.XL)
            .addComponent(panelSearchFields, IRDimension.Spacing.S)
            .addComponent(labelListOfApps, IRDimension.Spacing.L)
            .addComponent(scrollTablePanel, IRDimension.Spacing.S)
            .addComponentFillVertically(JPanel(), IRDimension.Spacing.M)
            .panel.apply {
                setOKButtonText(getMessage("label.button.delete"))
                setupPreferredSizeLarge()
            }

        return jPanel
    }

    private fun createSearchPackageNameTextField() {
        packagesTextFied = HintTextField(getMessage("msg.adb.hint.search.packages"))
        packagesTextFied.preferredSize = Dimension(IRDimension.Form.WIDTH_S, IRDimension.Field.HEIGHT_M)
        packagesTextFied.addKeyListener(object : IRKeyListener {
            override fun update(event: KeyEvent?) {
                event?.run { packageListener(event) }
            }
        })
    }

    override fun showForm() {
        show()
    }

    private fun setupSearchPackageNameButton() {
        searchButton = JButton(getMessage("label.button.search"))
        searchButton.addActionListener {
            val filter = packagesTextFied.text
            searchListener.invoke(
                SearchRemovePackagesCommand(filter, getMessage("msg.adb.label.success.search", filter))
            )
        }
    }

    private fun setupSearchPackagePanel() {
        panelSearchFields = JPanel(FlowLayout(FlowLayout.LEFT, 0, 0))
        panelSearchFields.apply {
            add(packagesTextFied)
            add(searchButton)
            minimumSize = Dimension(IRDimension.Form.WIDTH_M, IRDimension.Field.HEIGHT_M)
            preferredSize = Dimension(IRDimension.Form.WIDTH_M, IRDimension.Field.HEIGHT_M)
        }
    }

    private fun createSearchPackageName() {
        createSearchPackageNameTextField()
        setupSearchPackageNameButton()
        setupSearchPackagePanel()
    }

    private fun setupSearchTable() {
        searchTable = JBTable()
        searchTable?.isEnabled = false
        scrollTablePanel = JBScrollPane(searchTable)
        searchTable?.tableHeader?.reorderingAllowed = false

        updateTableData()
    }

    fun packageListener(e: KeyEvent) {
        ::jPanel.isInitialized.ifTrue {
            jPanel.apply {
                val textField = e.source as JTextField
                searchButton.isEnabled = textField.text.isNotEmpty()
            }
        }
    }

    override fun doOKAction() {
        searchedData.filter { it.isChecked }.map { it.title }.also {
            val command = SearchRemovePackagesCommand(it.joinToString(";"), it.joinToString("\n"))
            removeListener.invoke(command)
            super.doOKAction()
        }
    }

    private fun getMessage(property: String): String {
        return IridioBundle.getMessage(property)
    }

    private fun getMessage(key: String, value: String): String {
        return IridioBundle.message(key, value)
    }

    fun updateTable(list: List<PackagesModel>) {
        updateTableData(list, true)
        isOKActionEnabled = list.isNotEmpty()
        searchButton.isEnabled = true
    }

    private fun updateTableData(list: List<PackagesModel>? = emptyList(), isEnableTable: Boolean = false) {
        list?.run { searchedData = this }
        searchTable?.model = PackagesTableModel(searchedData)
        val columnModel = searchTable?.columnModel
        columnModel?.run {
            getColumn(COLUMN_CHECKBOX).cellRenderer = BooleanTableCellRenderer()
            getColumn(COLUMN_CHECKBOX).cellEditor = BooleanTableCellEditor()
        }

        searchTable?.run {
            setShowGrid(false)
            showVerticalLines = false
            intercellSpacing = Dimension(0, 0)
            preferredScrollableViewportSize = Dimension(IRDimension.Table.WIDTH_S, IRDimension.Table.HEIGHT_M)
            fillsViewportHeight = true
            isEnabled = isEnableTable
            setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION)
            updateUI()
        }
    }
}