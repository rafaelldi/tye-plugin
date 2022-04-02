package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.EnvironmentVariablesTab
import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.PortBindingsTab
import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.PropertiesTab
import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceRuntime
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ServiceDeploymentNodeComponent(private val runtime: TyeServiceRuntime<*>) : TyeDeploymentNodeComponent {
    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        val tabbedPane = JBTabbedPane()

        val propertiesTab = PropertiesTab(runtime.service.properties)
        tabbedPane.addTab(PropertiesTab.TITLE, propertiesTab.component)

        val portBindingsTab = PortBindingsTab(runtime.service.bindings)
        tabbedPane.addTab(PortBindingsTab.TITLE, portBindingsTab.component)

        if (runtime.service.environmentVariables != null) {
            val environmentVariablesTab = EnvironmentVariablesTab(runtime.service.environmentVariables)
            tabbedPane.addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
        }

        add(tabbedPane)
    }

    override fun getComponent(): JComponent = panel

    override fun update() {
    }
}