package com.github.rafaelldi.tyeplugin.startup

import com.github.rafaelldi.tyeplugin.actions.UpdateTyeGlobalToolNotificationAction
import com.github.rafaelldi.tyeplugin.util.TYE_ACTUAL_VERSION
import com.github.rafaelldi.tyeplugin.util.ToolVersion
import com.github.rafaelldi.tyeplugin.util.getTyeGlobalToolVersion
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

class TyeUpdateGlobalToolStartupActivity : StartupActivity, DumbAware {
    override fun runActivity(project: Project) {
        val installedVersion = getTyeGlobalToolVersion() ?: return
        val actualVersion = ToolVersion(TYE_ACTUAL_VERSION)

        if (installedVersion >= actualVersion) return

        Notification("Tye", "New version of tye global tool is available", "", NotificationType.INFORMATION)
            .addAction(UpdateTyeGlobalToolNotificationAction())
            .notify(project)
    }
}
