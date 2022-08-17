package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.EnvironmentVariablesTab
import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.MetricsTab
import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.PropertiesTab
import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.github.rafaelldi.tyeplugin.runtimes.TyeReplicaRuntime
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ReplicaDeploymentNodeComponent(private val runtime: TyeReplicaRuntime<*>) : TyeDeploymentNodeComponent {
    private val propertiesTab: PropertiesTab
    private val environmentVariablesTab: EnvironmentVariablesTab?
    private val metricsTab: MetricsTab?

    private val panel: JPanel = JPanel().apply {
        layout = BorderLayout()
        val tabbedPane = JBTabbedPane()

        propertiesTab = PropertiesTab(runtime.replica.properties)
        tabbedPane.addTab(PropertiesTab.TITLE, propertiesTab.component)

        if (runtime.replica.environmentVariables != null) {
            environmentVariablesTab = EnvironmentVariablesTab(runtime.replica.environmentVariables)
            tabbedPane.addTab(EnvironmentVariablesTab.TITLE, environmentVariablesTab.component)
        } else {
            environmentVariablesTab = null
        }

        if ((runtime.parent?.parent as? TyeApplicationRuntime)?.withMetrics == true) {
            metricsTab = MetricsTab()
            tabbedPane.addTab(MetricsTab.TITLE, metricsTab.component)
        } else {
            metricsTab = null
        }

        add(tabbedPane)
    }

    override fun getComponent(): JComponent = panel

    override fun update() {
        propertiesTab.update(runtime.replica.properties)
        if (runtime.replica.environmentVariables != null) {
            environmentVariablesTab?.update(runtime.replica.environmentVariables)
        }
        if (metricsTab != null) {
            val metrics = runtime.getReplicaMetrics()
            metricsTab.update(metrics)

        }
    }
}