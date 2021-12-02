package com.github.rafaelldi.tyeplugin.settings

import com.github.rafaelldi.tyeplugin.services.TyePathProvider
import com.intellij.openapi.components.BaseState

class TyeSettingsState : BaseState() {
    var tyeToolPath by string(TyePathProvider.getDefaultGlobalPath())
    var overwriteTyeFile by property(false)
    var checkTyeNewVersions by property(false)
}