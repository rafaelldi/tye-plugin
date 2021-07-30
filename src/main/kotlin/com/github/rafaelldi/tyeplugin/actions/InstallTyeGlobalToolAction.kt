package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeGlobalToolService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task

class InstallTyeGlobalToolAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Install tye global tool") {
            override fun run(indicator: ProgressIndicator) {
                val tyeGlobalToolService = project.service<TyeGlobalToolService>()

                indicator.isIndeterminate = true
                indicator.text = "Installing tye global tool"

                tyeGlobalToolService.installTyeGlobalTool()
            }
        }
        ProgressManager.getInstance().run(task)
    }
}
