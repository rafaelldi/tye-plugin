package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeApplication
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import kotlinx.coroutines.runBlocking

class ConnectToTyeHostAction : AnAction() {
    override fun update(e: AnActionEvent) {
        if (e.project != null) {
            val tyeApplication = e.project!!.service<TyeApplication>()
            e.presentation.isEnabledAndVisible = !tyeApplication.isConnected
        } else {
            e.presentation.isEnabledAndVisible = false
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Connecting to tye host") {
            override fun run(indicator: ProgressIndicator) {
                if (indicator.isCanceled) {
                    return
                }

                indicator.isIndeterminate = true
                indicator.text = "Connecting to tye host"

                connect(project!!)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun connect(project: Project) {
        val tyeApplication = project.service<TyeApplication>()
        runBlocking {
            tyeApplication.connect()
        }
    }
}