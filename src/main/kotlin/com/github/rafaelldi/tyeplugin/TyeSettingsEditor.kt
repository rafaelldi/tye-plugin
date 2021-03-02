package com.github.rafaelldi.tyeplugin

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.VerticalFlowLayout
import javax.swing.JComponent
import javax.swing.JPanel

class TyeSettingsEditor : SettingsEditor<TyeRunConfiguration>() {
    private lateinit var panel: JPanel

    override fun createEditor(): JComponent {
        createUIComponents()
        return panel
    }

    override fun resetEditorFrom(s: TyeRunConfiguration) {
    }

    override fun applyEditorTo(s: TyeRunConfiguration) {
    }

    private fun createUIComponents() {
        panel = JPanel().apply {
            layout = VerticalFlowLayout(VerticalFlowLayout.TOP)
        }
    }
}
