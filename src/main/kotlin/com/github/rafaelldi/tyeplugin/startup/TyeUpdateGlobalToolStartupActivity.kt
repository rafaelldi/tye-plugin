package com.github.rafaelldi.tyeplugin.startup

import com.github.rafaelldi.tyeplugin.actions.notification.DisableTyeNewVersionCheckAction
import com.github.rafaelldi.tyeplugin.actions.notification.UpdateTyeGlobalToolAction
import com.github.rafaelldi.tyeplugin.services.TyeGlobalToolService
import com.github.rafaelldi.tyeplugin.settings.TyeSettings
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class TyeUpdateGlobalToolStartupActivity : StartupActivity.DumbAware {
    override fun runActivity(project: Project) {
        val settings = TyeSettings.getInstance(project)
        if (!settings.checkTyeNewVersions)
            return

        val tyeGlobalToolService = project.service<TyeGlobalToolService>()
        val isTyeInstalled = tyeGlobalToolService.isTyeGlobalToolInstalled()
        if (!isTyeInstalled) return

        val isActualVersionInstalled = tyeGlobalToolService.isActualTyeGlobalToolVersionInstalled()
        if (isActualVersionInstalled) return

        Notification("Tye", "New version of tye global tool is available", "", NotificationType.INFORMATION)
            .addAction(UpdateTyeGlobalToolAction())
            .addAction(DisableTyeNewVersionCheckAction())
            .notify(project)
    }
}
