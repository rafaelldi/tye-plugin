package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.model.TyeServiceBinding
import com.github.rafaelldi.tyeplugin.model.TyeServiceEnvironmentVariable
import com.github.rafaelldi.tyeplugin.model.TyeServiceProperties
import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceRuntime
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.table.DefaultTableModel

class TyeDeploymentNodeComponent(deployment: Deployment) {
    private val panel: JPanel

    init {
        panel = JPanel().apply {
            layout = BorderLayout()

            val runtime = deployment.runtime
            if (runtime is TyeServiceRuntime<*>){
                val propertiesTab = PropertiesTab(runtime.service.properties)
                val portBindingsTab = PortBindingsTab(runtime.service.bindings)
                val environmentVariablesTab = EnvironmentVariablesTab(runtime.service.environmentVariables)

                val tabbedPane = JBTabbedPane().apply {
                    addTab(PropertiesTab.TITLE, propertiesTab.component)
                    addTab(PortBindingsTab.TITLE, portBindingsTab.component)
                    addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
                }
                add(tabbedPane)
            }
        }
    }

    fun getComponent(): JComponent = panel
}

private class PropertiesTab(properties: TyeServiceProperties) {
    companion object {
        const val TITLE = "Properties"
        private const val NAME_COLUMN_TITLE = "Name"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    val component: JComponent

    init {
        val table = DefaultTableModel(arrayOf(NAME_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

        with(table) {
            addRow(arrayOf("Id", properties.id))
            addRow(arrayOf("Type", properties.type))
            addRow(arrayOf("Replicas", properties.replicas))
            addRow(arrayOf("Restarts", properties.restarts))
            addRow(arrayOf("Project", properties.project))
            addRow(arrayOf("Image", properties.image))
            addRow(arrayOf("Executable", properties.executable))
            addRow(arrayOf("Working directory", properties.workingDirectory))
        }

        component = JBScrollPane(JBTable(table))
    }
}

private class PortBindingsTab(bindings: List<TyeServiceBinding>) {
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

        component = JBScrollPane(JBTable(table))
    }
}

private class EnvironmentVariablesTab(environmentVariables: List<TyeServiceEnvironmentVariable>) {
    companion object {
        const val TITLE = "Environment Variables"
        private const val NAME_COLUMN_TITLE = "Name"
        private const val VALUE_COLUMN_TITLE = "Value"
    }

    val component: JComponent

    init {
        val table = DefaultTableModel(arrayOf(NAME_COLUMN_TITLE, VALUE_COLUMN_TITLE), 0)

        for (variable in environmentVariables) {
            table.addRow(arrayOf(variable.name, variable.value))
        }

        component = JBScrollPane(JBTable(table))
    }
}