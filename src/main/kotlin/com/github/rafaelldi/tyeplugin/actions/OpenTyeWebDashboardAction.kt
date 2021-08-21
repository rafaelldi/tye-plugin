package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeApplicationManagerOld
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class OpenTyeWebDashboardAction : AnAction() {
    override fun update(e: AnActionEvent) {
        if (e.project != null) {
            val tyeApplication = e.project!!.service<TyeApplicationManagerOld>()
            e.presentation.isEnabledAndVisible = tyeApplication.isConnected
        } else {
            e.presentation.isEnabledAndVisible = false
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        if (e.project == null) return
        val settings = TyeSettingsState.getInstance(e.project!!)
        BrowserUtil.browse(settings.tyeHost)
    }
}
