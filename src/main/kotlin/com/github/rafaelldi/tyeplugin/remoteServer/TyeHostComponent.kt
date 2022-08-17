package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.COLUMNS_MEDIUM
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.columns
import com.intellij.ui.dsl.builder.panel
import javax.swing.JPanel

class TyeHostComponent {
    private val panel: JPanel
    private lateinit var tyeHostField: Cell<JBTextField>
    private lateinit var monitorMetricsField: Cell<JBCheckBox>

    init {
        panel = panel {
            row("Tye host address:") {
                tyeHostField = textField()
                    .columns(COLUMNS_MEDIUM)
            }
            row {
                monitorMetricsField = checkBox("Monitor service metrics")
            }
        }
    }

    fun getPanel(): JPanel = panel

    fun getHostAddress(): String = tyeHostField.component.text

    fun setHostAddress(host: String) {
        tyeHostField.component.text = host
    }

    fun getMonitorMetricsFlag(): Boolean = monitorMetricsField.component.isSelected

    fun setMonitorMetricsFlag(selected: Boolean) {
        monitorMetricsField.component.isSelected = selected
    }
}