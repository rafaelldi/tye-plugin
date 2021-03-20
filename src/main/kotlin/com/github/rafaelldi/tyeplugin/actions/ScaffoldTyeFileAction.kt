package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.github.rafaelldi.tyeplugin.tool.tyeInit
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class ScaffoldTyeFileAction : AnAction() {
    companion object {
        const val START_FRACTION: Double = 0.1
        const val FINISH_FRACTION: Double = 1.0
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Scaffold tye file") {
            override fun run(indicator: ProgressIndicator) {
                scaffoldTyeFile(e.project!!, indicator)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun scaffoldTyeFile(project: Project, indicator: ProgressIndicator) {
        if (indicator.isCanceled) {
            return
        }

        indicator.isIndeterminate = false
        indicator.fraction = START_FRACTION
        indicator.text = "Scaffolding tye.yaml file"

        val tyeToolPath = TyeSettingsState.getInstance(project).tyeToolPath
        if (tyeToolPath == null) {
            indicator.cancel()

            Notification(
                "tye.notifications.balloon",
                "Could not find tye tool",
                "Please install tye global tool or specify the path to it.",
                NotificationType.ERROR
            ).notify(project)
            return
        }

        tyeInit(tyeToolPath, project.basePath!!)

        indicator.fraction = FINISH_FRACTION

        Notification(
            "tye.notifications.toolWindow",
            "File tye.yaml is scaffolded",
            "",
            NotificationType.INFORMATION
        ).notify(project)
    }
}
