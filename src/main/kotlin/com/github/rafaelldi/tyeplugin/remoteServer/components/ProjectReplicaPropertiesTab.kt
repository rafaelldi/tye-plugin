package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.model.TyeProjectServiceReplica
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent
import javax.swing.table.DefaultTableModel

class ProjectReplicaPropertiesTab(replica: TyeProjectServiceReplica) : PropertiesTab()  {
    companion object {
        private const val NAME_COLUMN_TITLE = "Name"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    override val component: JComponent

    init {
        val table = DefaultTableModel(arrayOf(NAME_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

        with(table) {
            addRow(arrayOf("Name", replica.name))
            addRow(arrayOf("Pid", replica.pid))
            addRow(arrayOf("Exit code", replica.exitCode))
        }

        component = JBScrollPane(ValueTable(table))
    }
}