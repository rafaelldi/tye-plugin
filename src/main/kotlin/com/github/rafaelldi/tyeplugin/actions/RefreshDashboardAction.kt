package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeApiService
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking

class RefreshDashboardAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Update dashboard") {
            override fun run(indicator: ProgressIndicator) {
                updateDashboard(project!!)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun updateDashboard(project: Project) = runBlocking {
        val tyeService = project.service<TyeApiService>()
        val services = tyeService.getServices()
    }
}