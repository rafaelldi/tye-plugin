package com.github.rafaelldi.tyeplugin.settings

import com.github.rafaelldi.tyeplugin.util.findTyeGlobalToolPath
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.rafaelldi.tyeplugin.settings.TyeSettingsState",
    storages = [(Storage("TyePluginSettings.xml"))]
)
class TyeSettingsState : PersistentStateComponent<TyeSettingsState> {

    var tyeToolPath = findTyeGlobalToolPath()
    var tyeHost = "http://localhost:8000"
    var overwriteTyeFile = false

    companion object {
        @JvmStatic
        fun getInstance(project: Project): TyeSettingsState =
            ServiceManager.getService(project, TyeSettingsState::class.java)
    }

    override fun getState(): TyeSettingsState = this

    override fun loadState(state: TyeSettingsState) = XmlSerializerUtil.copyBean(state, this)
}
