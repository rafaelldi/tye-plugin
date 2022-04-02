package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class ValuesTable(values: MutableMap<String, String?>) : JBTable() {
    companion object {
        private const val NAME_COLUMN_TITLE = "Name"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    private val tableModel: DefaultTableModel = DefaultTableModel(arrayOf(NAME_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

    init {
        for (value in values) {
            tableModel.addRow(arrayOf(value.key, value.value))
        }

        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false
}