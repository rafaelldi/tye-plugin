package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsConfigurable
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil

class EditSettingsNotificationAction : NotificationAction("Edit settings") {
    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
        if (e.project == null) return
        ShowSettingsUtil.getInstance().editConfigurable(e.project, TyeSettingsConfigurable(e.project!!))
    }
}
