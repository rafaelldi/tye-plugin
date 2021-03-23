package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.github.rafaelldi.tyeplugin.tool.dotnetToolInstallTye
import com.github.rafaelldi.tyeplugin.tool.findTyeToolPath
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class InstallTyeToolAction : AnAction() {
    companion object {
        const val START_FRACTION: Double = 0.1
        const val HALF_FRACTION: Double = 0.5
        const val FINISH_FRACTION: Double = 1.0
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Install tye global tool") {
            override fun run(indicator: ProgressIndicator) {
                installTyeTool(e.project!!, indicator)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun installTyeTool(project: Project, indicator: ProgressIndicator) {
        if (indicator.isCanceled) {
            return
        }

        indicator.isIndeterminate = false
        indicator.fraction = START_FRACTION
        indicator.text = "Installing tye global tool"

        val success = dotnetToolInstallTye()

        indicator.fraction = HALF_FRACTION

        if (success) {
            TyeSettingsState.getInstance(project).tyeToolPath = findTyeToolPath()
            Notification(
                "tye.notifications.toolWindow",
                "Tye tool is successfully installed",
                "",
                NotificationType.INFORMATION
            ).notify(project)
        } else {
            Notification(
                "tye.notifications.balloon",
                "Tye installation failed",
                "",
                NotificationType.ERROR
            ).notify(project)
        }

        indicator.fraction = FINISH_FRACTION
    }
}
