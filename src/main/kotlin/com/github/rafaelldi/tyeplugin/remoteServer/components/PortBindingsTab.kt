package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.model.TyeServiceBinding
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent
import javax.swing.table.DefaultTableModel

class PortBindingsTab(bindings: List<TyeServiceBinding>) {
    companion object {
        const val TITLE = "Port Bindings"
        private const val NAME_COLUMN_TITLE = "Name"
        private const val PROTOCOL_COLUMN_TITLE = "Protocol"
        private const val HOST_COLUMN_TITLE = "Host"
        private const val PORT_COLUMN_TITLE = "Port"
        private const val CONTAINER_PORT_COLUMN_TITLE = "Container port"
        private const val CONNECTION_STRING_COLUMN_TITLE = "Connection string"
    }

    val component: JComponent

    init {
        val table = DefaultTableModel(
            arrayOf(
                NAME_COLUMN_TITLE,
                PROTOCOL_COLUMN_TITLE,
                HOST_COLUMN_TITLE,
                PORT_COLUMN_TITLE,
                CONTAINER_PORT_COLUMN_TITLE,
                CONNECTION_STRING_COLUMN_TITLE
            ), 0
        )

        for (bind in bindings) {
            table.addRow(
                arrayOf(
                    bind.name,
                    bind.protocol,
                    bind.host,
                    bind.port,
                    bind.containerPort,
                    bind.connectionString
                )
            )
        }

        component = JBScrollPane(ValueTable(table))
    }
}