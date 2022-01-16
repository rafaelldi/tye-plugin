package com.github.rafaelldi.tyeplugin.actions.notification

import com.github.rafaelldi.tyeplugin.services.TyeGlobalToolService
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.runBackgroundableTask

class UpdateTyeGlobalToolAction : NotificationAction("Update tye global tool") {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        val tyeGlobalToolService = e.project!!.service<TyeGlobalToolService>()

        runBackgroundableTask("Update Tye Global Tool", e.project) {
            it.isIndeterminate = true
            it.text = "Updating tye global tool"

            tyeGlobalToolService.updateTyeGlobalTool()
        }
    }
}
