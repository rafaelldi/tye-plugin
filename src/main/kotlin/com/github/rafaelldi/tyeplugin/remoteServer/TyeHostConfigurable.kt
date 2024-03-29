package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.remoteServer.util.CloudConfigurableBase
import javax.swing.JComponent
import javax.swing.JPasswordField
import javax.swing.JTextField

class TyeHostConfigurable(configuration: TyeHostConfiguration) :
    CloudConfigurableBase<TyeHostConfiguration>(TyeHostType.getInstance(), configuration) {
    private val component = TyeHostComponent()
    private var emailTextField: JTextField? = null
    private var passwordField: JPasswordField? = null

    override fun getMainPanel(): JComponent = component.getPanel()

    override fun getEmailTextField(): JTextField? = emailTextField

    override fun getPasswordField(): JPasswordField? = passwordField

    override fun reset() {
        component.setHostAddress(myConfiguration.hostAddress)
        component.setMonitorMetricsFlag(myConfiguration.monitorMetrics)
    }

    override fun isModified(): Boolean = component.getHostAddress() != myConfiguration.hostAddress ||
            component.getMonitorMetricsFlag() != myConfiguration.monitorMetrics

    override fun applyCoreTo(configuration: TyeHostConfiguration?, forComparison: Boolean) {
        myConfiguration.hostAddress = component.getHostAddress()
        myConfiguration.monitorMetrics = component.getMonitorMetricsFlag()
    }

    override fun canCheckConnection(): Boolean = false
}