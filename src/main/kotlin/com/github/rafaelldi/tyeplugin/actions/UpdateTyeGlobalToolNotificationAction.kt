package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeGlobalToolService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task

class UpdateTyeGlobalToolNotificationAction : NotificationAction("Update tye global tool") {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val task = object : Task.Backgroundable(e.project, "Update tye global tool") {
            override fun run(indicator: ProgressIndicator) {
                val tyeGlobalToolService = project.service<TyeGlobalToolService>()

                indicator.isIndeterminate = true
                indicator.text = "Updating tye global tool"

                tyeGlobalToolService.updateTyeGlobalTool()
            }
        }
        ProgressManager.getInstance().run(task)
    }
}
