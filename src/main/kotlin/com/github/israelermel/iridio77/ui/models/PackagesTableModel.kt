package com.github.israelermel.iridio77.ui.models

import javax.swing.table.AbstractTableModel

const val COLUMN_NAME = 0
const val COLUMN_CHECKBOX = 1
private const val COLUMN_NAME_TITLE = "Packages(Apps)"
private const val COLUMN_NAME_CHECKBOX = "Select to Remove"

class PackagesTableModel(
    private val data: List<PackagesModel> = emptyList()
) : AbstractTableModel() {
    private val columnNames = arrayOf(COLUMN_NAME_TITLE, COLUMN_NAME_CHECKBOX)
    override fun getRowCount() = data.size
    override fun getColumnCount() = columnNames.size
    override fun getColumnName(column: Int) = columnNames[column]

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        return when (columnIndex) {
            COLUMN_NAME -> data[rowIndex].title
            COLUMN_CHECKBOX -> data[rowIndex].isChecked
            else -> null
        }
    }

    override fun getColumnClass(columnIndex: Int): Class<*>? {
        return when (columnIndex) {
            COLUMN_NAME -> String::class.java
            COLUMN_CHECKBOX -> Boolean::class.java
            else -> null
        }
    }

    override fun isCellEditable(row: Int, col: Int) = col == COLUMN_CHECKBOX
    override fun setValueAt(value: Any?, row: Int, col: Int) {
        if (col == COLUMN_CHECKBOX && value is Boolean) {
            data[row].isChecked = value
            fireTableCellUpdated(row, col)
        }
    }
}

data class PackagesModel(
    var title: String, var isChecked: Boolean = true
)