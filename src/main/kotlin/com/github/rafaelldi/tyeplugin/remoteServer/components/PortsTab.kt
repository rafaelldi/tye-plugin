package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent
import javax.swing.table.DefaultTableModel

class PortsTab(ports: List<Int>?) {
    companion object {
        const val TITLE = "Ports"
        private const val PORT_COLUMN_TITLE = "Port"
    }

    val component: JComponent

    init {
        val table = DefaultTableModel(arrayOf(PORT_COLUMN_TITLE), 0)

        for (port in ports ?: emptyList()) {
            table.addRow(arrayOf(port))
        }

        component = JBScrollPane(ValueTable(table))
    }
}