package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.runtimes.TyeReplicaRuntime
import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceRuntime
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class TyeDeploymentNodeComponent(deployment: Deployment) {
    private val panel: JPanel

    init {
        panel = JPanel().apply {
            layout = BorderLayout()

            val runtime = deployment.runtime
            if (runtime is TyeServiceRuntime<*>) {
                val propertiesTab = when (runtime.service) {
                    is TyeContainerService -> ContainerServicePropertiesTab(runtime.service.properties as TyeContainerServiceProperties)
                    is TyeExecutableService -> ExecutableServicePropertiesTab(runtime.service.properties as TyeExecutableServiceProperties)
                    is TyeProjectService -> ProjectServicePropertiesTab(runtime.service.properties as TyeProjectServiceProperties)
                }
                val portBindingsTab = PortBindingsTab(runtime.service.bindings)
                val environmentVariablesTab = EnvironmentVariablesTab(runtime.service.environmentVariables)

                val tabbedPane = JBTabbedPane().apply {
                    addTab(propertiesTab.tabName, propertiesTab.component)
                    addTab(PortBindingsTab.TITLE, portBindingsTab.component)
                    addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
                }
                add(tabbedPane)
            } else if (runtime is TyeReplicaRuntime<*>) {
                val propertiesTab = when(runtime.replica) {
                    is TyeContainerServiceReplica -> ContainerReplicaPropertiesTab(runtime.replica)
                    is TyeProjectServiceReplica -> ProjectReplicaPropertiesTab(runtime.replica)
                }

                val environmentVariablesTab = EnvironmentVariablesTab(runtime.replica.environmentVariables)

                val tabbedPane = JBTabbedPane().apply {
                    addTab(propertiesTab.tabName, propertiesTab.component)
                    addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
                }
                add(tabbedPane)
            }
        }
    }

    fun getComponent(): JComponent = panel
}

