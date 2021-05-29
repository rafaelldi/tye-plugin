package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class OpenTyeWebDashboardAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) return
        val settings = TyeSettingsState.getInstance(e.project!!)
        BrowserUtil.browse(settings.tyeHost)
    }
}