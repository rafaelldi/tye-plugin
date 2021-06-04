package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.util.updateTyeGlobalTool
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class UpdateTyeGlobalToolNotificationAction : NotificationAction("Update") {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val task = object : Task.Backgroundable(e.project, "Update tye global tool") {
            override fun run(indicator: ProgressIndicator) {
                if (indicator.isCanceled) {
                    return
                }

                indicator.isIndeterminate = true
                indicator.text = "Updating tye global tool"

                updateTyeTool(e.project!!)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun updateTyeTool(project: Project) {
        if (updateTyeGlobalTool()) {
            Notification("Tye", "Tye is successfully updated", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            Notification("Tye", "Tye update failed", "", NotificationType.ERROR)
                .notify(project)
        }
    }
}
