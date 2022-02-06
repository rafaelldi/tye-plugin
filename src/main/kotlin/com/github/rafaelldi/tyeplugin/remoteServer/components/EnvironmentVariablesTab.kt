package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.model.TyeEnvironmentVariable
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent
import javax.swing.table.DefaultTableModel

class EnvironmentVariablesTab(environmentVariables: List<TyeEnvironmentVariable>) {
    companion object {
        const val TITLE = "Environment Variables"
        private const val NAME_COLUMN_TITLE = "Name"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    val component: JComponent

    init {
        val table = DefaultTableModel(arrayOf(NAME_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

        for (variable in environmentVariables ?: emptyList()) {
            table.addRow(arrayOf(variable.name, variable.value))
        }

        component = JBScrollPane(ValueTable(table))
    }
}