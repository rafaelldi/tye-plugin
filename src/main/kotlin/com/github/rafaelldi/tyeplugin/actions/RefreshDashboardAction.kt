package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeApiService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service

class RefreshDashboardAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val tyeService = project!!.service<TyeApiService>()
        val services = tyeService.getServices()
    }
}