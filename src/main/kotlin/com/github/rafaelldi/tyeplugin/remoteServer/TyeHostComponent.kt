package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.panel
import javax.swing.JPanel

class TyeHostComponent {
    private var panel: JPanel
    private var tyeHostField: JBTextField = JBTextField()

    init {
        panel = panel {
            row("Tye host address:") {
                tyeHostField()
            }
        }
    }

    fun getPanel(): JPanel = panel

    fun getHostAddress(): String = tyeHostField.text

    fun setHostAddress(host: String) {
        tyeHostField.text = host
    }
}