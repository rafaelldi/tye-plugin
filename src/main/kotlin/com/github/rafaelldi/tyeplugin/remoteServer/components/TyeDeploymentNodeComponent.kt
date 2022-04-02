package com.github.rafaelldi.tyeplugin.remoteServer.components

import javax.swing.JComponent

interface TyeDeploymentNodeComponent {
    fun getComponent(): JComponent
    fun update() {}
}

