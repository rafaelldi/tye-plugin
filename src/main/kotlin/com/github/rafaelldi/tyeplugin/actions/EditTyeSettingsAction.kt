package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsConfigurable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class EditTyeSettingsAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) return
        ShowSettingsUtil.getInstance().editConfigurable(e.project, TyeSettingsConfigurable(e.project!!))
    }
}