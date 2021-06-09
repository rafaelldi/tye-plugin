package com.github.rafaelldi.tyeplugin.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class InstallTyeGlobalToolAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Install tye global tool") {
            override fun run(indicator: ProgressIndicator) {
                if (indicator.isCanceled) {
                    return
                }

                if (!checkDotNetInstalled(e.project!!)) return
                if (!checkTyeNotInstalled(e.project!!)) return

                indicator.isIndeterminate = true
                indicator.text = "Installing tye global tool"

                installTyeTool(e.project!!)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun installTyeTool(project: Project) {
        installTye(project)
    }
}
