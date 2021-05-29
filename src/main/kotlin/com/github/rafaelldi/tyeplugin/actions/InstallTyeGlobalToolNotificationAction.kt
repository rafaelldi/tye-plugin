package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.util.installGlobalTool
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class InstallTyeGlobalToolNotificationAction : NotificationAction("Install tye tool") {
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

        installGlobalTool(project)
    }
}
