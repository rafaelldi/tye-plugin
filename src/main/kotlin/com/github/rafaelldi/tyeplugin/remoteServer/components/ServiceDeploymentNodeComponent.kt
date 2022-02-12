package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceRuntime
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ServiceDeploymentNodeComponent(private val runtime: TyeServiceRuntime<*>): TyeDeploymentNodeComponent {
    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()

        val propertiesTab = when (runtime.service) {
            is TyeContainerService -> ContainerServicePropertiesTab(runtime.service.properties as TyeContainerServiceProperties)
            is TyeExecutableService -> ExecutableServicePropertiesTab(runtime.service.properties as TyeExecutableServiceProperties)
            is TyeProjectService -> ProjectServicePropertiesTab(runtime.service.properties as TyeProjectServiceProperties)
            is TyeIngressService -> IngressServicePropertiesTab(runtime.service.properties as TyeIngressServiceProperties)
        }
        val portBindingsTab = PortBindingsTab(runtime.service.bindings)
        val environmentVariablesTab =
            if (runtime.service.environmentVariables != null)
                EnvironmentVariablesTab(runtime.service.environmentVariables)
            else null

        val tabbedPane = JBTabbedPane().apply {
            addTab(PropertiesTab.TITLE, propertiesTab.component)
            addTab(PortBindingsTab.TITLE, portBindingsTab.component)
            if (environmentVariablesTab != null) {
                addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
            }
        }
        add(tabbedPane)
    }

    override fun getComponent(): JComponent = panel
}