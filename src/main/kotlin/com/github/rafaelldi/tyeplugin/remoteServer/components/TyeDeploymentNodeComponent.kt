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

            when (val runtime = deployment.runtime) {
                is TyeServiceRuntime<*> -> {
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
                        addTab(propertiesTab.tabName, propertiesTab.component)
                        addTab(PortBindingsTab.TITLE, portBindingsTab.component)
                        if (environmentVariablesTab != null) {
                            addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
                        }
                    }
                    add(tabbedPane)
                }
                is TyeReplicaRuntime<*> -> {
                    val propertiesTab = when (runtime.replica) {
                        is TyeContainerServiceReplica -> ContainerReplicaPropertiesTab(runtime.replica)
                        is TyeProjectServiceReplica -> ProjectReplicaPropertiesTab(runtime.replica)
                        is TyeIngressServiceReplica -> IngressReplicaPropertiesTab(runtime.replica)
                    }

                    val environmentVariablesTab =
                        if (runtime.replica.environmentVariables != null)
                            EnvironmentVariablesTab(runtime.replica.environmentVariables)
                        else null

                    val tabbedPane = JBTabbedPane().apply {
                        addTab(propertiesTab.tabName, propertiesTab.component)
                        if (environmentVariablesTab != null) {
                            addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
                        }
                    }
                    add(tabbedPane)
                }
            }
        }
    }

    fun getComponent(): JComponent = panel
}

