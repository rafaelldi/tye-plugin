package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeGlobalToolService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.runBackgroundableTask

class InstallTyeGlobalToolAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val tyeGlobalToolService = e.project!!.service<TyeGlobalToolService>()

        runBackgroundableTask("Install tye global tool", e.project) {
            it.isIndeterminate = true
            it.text = "Installing tye global tool"

            tyeGlobalToolService.installTyeGlobalTool()
        }
    }
}
