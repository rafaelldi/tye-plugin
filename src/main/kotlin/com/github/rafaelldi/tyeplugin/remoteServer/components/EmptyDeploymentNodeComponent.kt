package com.github.rafaelldi.tyeplugin.remoteServer.components

import javax.swing.JComponent
import javax.swing.JPanel

class EmptyDeploymentNodeComponent: TyeDeploymentNodeComponent {
    private val panel: JPanel = JPanel()

    override fun getComponent(): JComponent = panel
}