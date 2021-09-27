package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.VerticalFlowLayout
import com.intellij.ui.components.JBTextField
import java.awt.BorderLayout
import javax.swing.JPanel

class TyeHostComponent {
    private lateinit var panel: JPanel
    private lateinit var tyeHostField: LabeledComponent<JBTextField>

    init {
        createUIComponents()
    }

    fun getPanel(): JPanel = panel

    fun getHostAddress(): String = tyeHostField.component.text

    fun setHostAddress(host: String) {
        tyeHostField.component.text = host
    }

    private fun createUIComponents() {
        panel = JPanel().apply {
            layout = VerticalFlowLayout(VerticalFlowLayout.TOP)

            val tyeHostTextField = JBTextField()
            tyeHostField = LabeledComponent.create(tyeHostTextField, "Tye host address")
            tyeHostField.labelLocation = BorderLayout.WEST
            add(tyeHostField)
        }
    }
}