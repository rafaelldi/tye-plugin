package com.github.rafaelldi.tyeplugin.settings

import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

@State(
    name = "com.github.rafaelldi.tyeplugin.settings.TyeSettingsState",
    storages = [(Storage("TyePluginSettings.xml"))]
)
class TyeSettings : SimplePersistentStateComponent<TyeSettingsState>(TyeSettingsState()) {
    companion object {
        @JvmStatic
        fun getInstance(project: Project): TyeSettings = project.service()
    }

    var tyeToolPath
        get() = state.tyeToolPath ?: ""
        set(value) {
            state.tyeToolPath = value
        }

    var overwriteTyeFile
        get() = state.overwriteTyeFile
        set(value) {
            state.overwriteTyeFile = value
        }

    var checkTyeNewVersions
        get() = state.checkTyeNewVersions
        set(value) {
            state.checkTyeNewVersions = value
        }
}
