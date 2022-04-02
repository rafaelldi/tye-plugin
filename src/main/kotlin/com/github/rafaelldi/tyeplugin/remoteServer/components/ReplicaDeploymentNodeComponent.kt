package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.EnvironmentVariablesTab
import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.PropertiesTab
import com.github.rafaelldi.tyeplugin.runtimes.TyeReplicaRuntime
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ReplicaDeploymentNodeComponent(private val runtime: TyeReplicaRuntime<*>) : TyeDeploymentNodeComponent {
    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        val tabbedPane = JBTabbedPane()

        val propertiesTab = PropertiesTab(runtime.replica.properties)
        tabbedPane.addTab(PropertiesTab.TITLE, propertiesTab.component)

        if (runtime.replica.environmentVariables != null) {
            val environmentVariablesTab = EnvironmentVariablesTab(runtime.replica.environmentVariables)
            tabbedPane.addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
        }

        add(tabbedPane)
    }

    override fun getComponent(): JComponent = panel
}