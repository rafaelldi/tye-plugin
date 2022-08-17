package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class MetricsTable : JBTable() {
    companion object {
        private const val COUNTER_COLUMN_TITLE = "Counter"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    private val metricRowMap: MutableMap<String, Int> = mutableMapOf()
    private val tableModel: DefaultTableModel =
        DefaultTableModel(arrayOf(COUNTER_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

    init {
        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false

    fun update(metrics: Map<String, String?>) {
        for (metric in metrics) {
            if (metricRowMap.containsKey(metric.key)) {
                val row = metricRowMap[metric.key]!!
                val currentValue = tableModel.getValueAt(row, 1)
                if (currentValue != metric.value) {
                    tableModel.setValueAt(metric.value, row, 1)
                }
            } else {
                tableModel.addRow(arrayOf(metric.key, metric.value))
                val index = (metricRowMap.values.maxOrNull() ?: -1) + 1
                metricRowMap[metric.key] = index
            }
        }
    }
}