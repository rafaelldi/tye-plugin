package com.github.rafaelldi.tyeplugin.remoteServer.components.tabs

import com.github.rafaelldi.tyeplugin.remoteServer.components.ValuesTable
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent

class EnvironmentVariablesTab(environmentVariables: MutableMap<String, String?>) {
    companion object {
        const val TITLE = "Environment Variables"
    }

    val component: JComponent
    private val table: ValuesTable

    init {
        table = ValuesTable(environmentVariables)
        component = JBScrollPane(table)
    }

    fun update(environmentVariables: MutableMap<String, String?>) {
        table.update(environmentVariables)
    }
}