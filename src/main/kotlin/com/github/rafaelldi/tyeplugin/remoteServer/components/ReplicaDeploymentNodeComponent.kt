package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.model.TyeContainerServiceReplica
import com.github.rafaelldi.tyeplugin.model.TyeIngressServiceReplica
import com.github.rafaelldi.tyeplugin.model.TyeProjectServiceReplica
import com.github.rafaelldi.tyeplugin.runtimes.TyeReplicaRuntime
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ReplicaDeploymentNodeComponent(private val runtime: TyeReplicaRuntime<*>): TyeDeploymentNodeComponent {
    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()

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
            addTab(PropertiesTab.TITLE, propertiesTab.component)
            if (environmentVariablesTab != null) {
                addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
            }
        }
        add(tabbedPane)
    }

    override fun getComponent(): JComponent = panel
}