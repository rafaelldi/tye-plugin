package com.github.rafaelldi.tyeplugin.startup

import com.github.rafaelldi.tyeplugin.actions.DisableTyeNewVersionCheckAction
import com.github.rafaelldi.tyeplugin.actions.UpdateTyeGlobalToolNotificationAction
import com.github.rafaelldi.tyeplugin.services.TyeGlobalToolService
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class TyeUpdateGlobalToolStartupActivity : StartupActivity, DumbAware {
    override fun runActivity(project: Project) {
        val settings = TyeSettingsState.getInstance(project)
        if (!settings.checkTyeNewVersions)
            return

        val tyeGlobalToolService = project.service<TyeGlobalToolService>()
        val isActualVersionInstalled = tyeGlobalToolService.isActualTyeGlobalToolVersionInstalled()

        if (isActualVersionInstalled) return

        Notification("Tye", "New version of tye global tool is available", "", NotificationType.INFORMATION)
            .addAction(UpdateTyeGlobalToolNotificationAction())
            .addAction(DisableTyeNewVersionCheckAction())
            .notify(project)
    }
}
