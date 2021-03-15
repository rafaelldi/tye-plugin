package com.github.rafaelldi.tyeplugin.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

class TyeSettingsConfigurable(private val project: Project) : Configurable {
    private val component = TyeSettingsComponent()

    override fun createComponent(): JComponent = component.getPanel()

    override fun getPreferredFocusedComponent(): JComponent = component.getPreferredFocusedComponent()

    override fun isModified(): Boolean {
        val settings = TyeSettingsState.getInstance(project)
        return component.getTyeToolPath() != settings.tyeToolPath
    }

    override fun apply() {
        val settings = TyeSettingsState.getInstance(project)
        settings.tyeToolPath = component.getTyeToolPath()
    }

    override fun reset() {
        val settings = TyeSettingsState.getInstance(project)
        component.setTyeToolPath(settings.tyeToolPath)
    }

    override fun getDisplayName(): String = "Tye"
}
