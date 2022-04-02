package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class ValuesTable(values: MutableMap<String, String?>) : JBTable() {
    companion object {
        private const val NAME_COLUMN_TITLE = "Name"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    private val keyRowMap: MutableMap<String, Int> = mutableMapOf()
    private val tableModel: DefaultTableModel = DefaultTableModel(arrayOf(NAME_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

    init {
        var index = 0
        for (value in values) {
            tableModel.addRow(arrayOf(value.key, value.value))
            keyRowMap[value.key] = index
            index++
        }

        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false

    fun update(values: MutableMap<String, String?>) {
        val deletedRows = keyRowMap.keys.subtract(values.keys)

        for (value in values) {
            if (keyRowMap.containsKey(value.key)) {
                val row = keyRowMap[value.key]!!
                val currentValue = tableModel.getValueAt(row, 1)
                if (currentValue != value.value) {
                    tableModel.setValueAt(value.value, row, 1)
                }
            } else {
                tableModel.addRow(arrayOf(value.key, value.value))
                val index = (keyRowMap.values.maxOrNull() ?: -1) + 1
                keyRowMap[value.key] = index
            }
        }

        for (deletedRow in deletedRows.sortedByDescending { it }) {
            val row = keyRowMap[deletedRow]!!
            tableModel.removeRow(row)
            keyRowMap.remove(deletedRow)
        }
    }
}