package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.github.rafaelldi.tyeplugin.utils.dotnetToolInstallTye
import com.github.rafaelldi.tyeplugin.utils.findTyeToolPath
import com.github.rafaelldi.tyeplugin.utils.isDotnetInstalled
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class InstallTyeGlobalToolNotificationAction(text: String?) : NotificationAction(text) {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
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

        indicator.isIndeterminate = true
        indicator.text = "Installing tye global tool"

        if (!isDotnetInstalled()) {
            Notification(
                "tye.notifications.balloon",
                ".NET Core 3.1 is not installed",
                "",
                NotificationType.ERROR
            ).notify(project)
            return
        }

        if (dotnetToolInstallTye()) {
            TyeSettingsState.getInstance(project).tyeToolPath = findTyeToolPath()
            Notification(
                "tye.notifications.balloon",
                "Tye is successfully installed",
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
    }
}
