package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.model.TyeExecutableServiceProperties
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent
import javax.swing.table.DefaultTableModel

class ExecutableServicePropertiesTab(properties: TyeExecutableServiceProperties) : PropertiesTab() {
    companion object {
        private const val NAME_COLUMN_TITLE = "Name"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    override val component: JComponent

    init {
        val table = DefaultTableModel(arrayOf(NAME_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

        with(table) {
            addRow(arrayOf("Id", properties.id))
            addRow(arrayOf("Type", properties.type))
            addRow(arrayOf("Source", properties.source))
            addRow(arrayOf("Replicas", properties.replicas))
            addRow(arrayOf("Restarts", properties.restarts))
            addRow(arrayOf("Executable", properties.executable))
            addRow(arrayOf("Working directory", properties.workingDirectory))
            addRow(arrayOf("Args", properties.args))
        }

        component = JBScrollPane(ValueTable(table))
    }
}