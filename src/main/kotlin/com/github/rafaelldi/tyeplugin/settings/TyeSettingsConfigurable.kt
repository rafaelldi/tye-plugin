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
        return (component.getTyeToolPath() != settings.tyeToolPath) or
                (component.getOverwriteTyeFile() != settings.overwriteTyeFile) or
                (component.getCheckTyeNewVersions() != settings.checkTyeNewVersions)
    }

    override fun apply() {
        val settings = TyeSettingsState.getInstance(project)
        settings.tyeToolPath = component.getTyeToolPath()
        settings.tyeHost = component.getTyeHost()
        settings.overwriteTyeFile = component.getOverwriteTyeFile()
        settings.checkTyeNewVersions = component.getCheckTyeNewVersions()
    }

    override fun reset() {
        val settings = TyeSettingsState.getInstance(project)
        component.setTyeToolPath(settings.tyeToolPath)
        component.setTyeHost(settings.tyeHost)
        component.setOverwriteTyeFile(settings.overwriteTyeFile)
        component.setCheckTyeNewVersions(settings.checkTyeNewVersions)
    }

    override fun getDisplayName(): String = "Tye"
}
