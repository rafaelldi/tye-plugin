package com.github.rafaelldi.tyeplugin.remoteServer.components

import javax.swing.JComponent

abstract class PropertiesTab {
    companion object {
        private const val TITLE = "Properties"
    }

    val tabName: String = TITLE
    abstract val component: JComponent
}