package com.github.rafaelldi.tyeplugin.remoteServer.components.tabs

import com.github.rafaelldi.tyeplugin.model.TyeServiceBinding
import com.github.rafaelldi.tyeplugin.remoteServer.components.PortBindingsTable
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent

class PortBindingsTab(bindings: List<TyeServiceBinding>) {
    companion object {
        const val TITLE = "Port Bindings"
    }

    val component: JComponent
    private val table: PortBindingsTable

    init {
        table = PortBindingsTable(bindings)
        component = JBScrollPane(table)
    }
}