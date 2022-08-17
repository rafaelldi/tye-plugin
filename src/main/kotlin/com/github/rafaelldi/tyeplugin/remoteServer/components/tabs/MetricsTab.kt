package com.github.rafaelldi.tyeplugin.remoteServer.components.tabs

import com.github.rafaelldi.tyeplugin.remoteServer.components.MetricsTable
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent

class MetricsTab {
    companion object {
        const val TITLE = "Metrics"
    }

    val component: JComponent
    private val table: MetricsTable = MetricsTable()

    init {
        component = JBScrollPane(table)
    }

    fun update(metrics: Map<String, String?>) {
        table.update(metrics)
    }
}