package com.github.rafaelldi.tyeplugin.remoteServer.components

import javax.swing.JComponent

abstract class PropertiesTab {
    companion object {
        const val TITLE = "Properties"
    }

    abstract val component: JComponent
}