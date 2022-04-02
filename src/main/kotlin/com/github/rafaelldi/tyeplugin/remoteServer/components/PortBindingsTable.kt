package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.model.TyeServiceBinding
import com.intellij.ui.table.JBTable
import javax.swing.table.DefaultTableModel

class PortBindingsTable(bindings: List<TyeServiceBinding>) : JBTable() {
    companion object {
        private const val NAME_COLUMN_TITLE = "Name"
        private const val PROTOCOL_COLUMN_TITLE = "Protocol"
        private const val HOST_COLUMN_TITLE = "Host"
        private const val PORT_COLUMN_TITLE = "Port"
        private const val CONTAINER_PORT_COLUMN_TITLE = "Container port"
        private const val CONNECTION_STRING_COLUMN_TITLE = "Connection string"
    }

    private val tableModel: DefaultTableModel = DefaultTableModel(
        arrayOf(
            NAME_COLUMN_TITLE,
            PROTOCOL_COLUMN_TITLE,
            HOST_COLUMN_TITLE,
            PORT_COLUMN_TITLE,
            CONTAINER_PORT_COLUMN_TITLE,
            CONNECTION_STRING_COLUMN_TITLE
        ), 0
    )

    init {
        for (bind in bindings) {
            tableModel.addRow(
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

        model = tableModel
    }

    override fun isCellEditable(row: Int, column: Int): Boolean = false
}