package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeApplicationManagerOld
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking

class TyeApplicationShutdownAction : AnAction() {
    override fun update(e: AnActionEvent) {
        if (e.project != null) {
            val tyeApplication = e.project!!.service<TyeApplicationManagerOld>()
            e.presentation.isEnabledAndVisible = tyeApplication.isConnected
        } else {
            e.presentation.isEnabledAndVisible = false
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Tye shutdown") {
            override fun run(indicator: ProgressIndicator) {
                indicator.isIndeterminate = true
                indicator.text = "Tye application is stopping"

                shutdown(project!!)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun shutdown(project: Project) {
        val tyeApplication = project.service<TyeApplicationManagerOld>()
        runBlocking {
            tyeApplication.shutdown()
        }
    }
}
