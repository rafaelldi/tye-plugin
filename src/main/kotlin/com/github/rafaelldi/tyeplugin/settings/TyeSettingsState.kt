package com.github.rafaelldi.tyeplugin.settings

import com.github.rafaelldi.tyeplugin.services.TyePathProvider
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializerUtil

@State(
    name = "com.github.rafaelldi.tyeplugin.settings.TyeSettingsState",
    storages = [(Storage("TyePluginSettings.xml"))]
)
class TyeSettingsState : PersistentStateComponent<TyeSettingsState> {
    companion object {
        private const val DEFAULT_HOST_ADDRESS = "http://localhost:8000"

        @JvmStatic
        fun getInstance(project: Project): TyeSettingsState = project.service()
    }

    var tyeToolPath: String = TyePathProvider.getDefaultGlobalPath()
    var tyeHost = DEFAULT_HOST_ADDRESS
    var overwriteTyeFile = false
    var checkTyeNewVersions = false

    override fun getState(): TyeSettingsState = this

    override fun loadState(state: TyeSettingsState) = XmlSerializerUtil.copyBean(state, this)
}
