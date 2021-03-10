package com.github.rafaelldi.tyeplugin.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "TyePluginSettings", storages = [(Storage("TyePluginSettings.xml"))])
class TyeSettings : PersistentStateComponent<TyeSettings> {

    companion object {
        @JvmStatic
        fun getInstance(): TyeSettings = ServiceManager.getService(TyeSettings::class.java)
    }

    var tyeTool = "~/.dotnet/tools/tye"

    override fun getState(): TyeSettings = this

    override fun loadState(state: TyeSettings) = XmlSerializerUtil.copyBean(state, this)
}
