package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.github.rafaelldi.tyeplugin.util.findTyeGlobalToolPath
import com.github.rafaelldi.tyeplugin.util.installTyeGlobalTool
import com.github.rafaelldi.tyeplugin.util.isDotNetInstalled
import com.github.rafaelldi.tyeplugin.util.isTyeGlobalToolInstalled
import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project

fun checkDotNetInstalled(project: Project): Boolean {
    if (isDotNetInstalled()) return true

    Notification("Tye", ".NET Core 3.1 is not installed", "", NotificationType.ERROR)
        .addAction(object : NotificationAction("Go to .NET Core installation page") {
            override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                BrowserUtil.browse("https://dotnet.microsoft.com/download/dotnet/3.1")
            }
        })
        .notify(project)

    return false
}

fun checkTyeNotInstalled(project: Project): Boolean {
    if (!isTyeGlobalToolInstalled()) return true

    Notification("Tye", "Tye is already installed", "", NotificationType.WARNING)
        .notify(project)

    return false
}

fun installTye(project: Project) {
    if (installTyeGlobalTool()) {
        TyeSettingsState.getInstance(project).tyeToolPath = findTyeGlobalToolPath()
        Notification("Tye", "Tye is successfully installed", "", NotificationType.INFORMATION)
            .notify(project)
    } else {
        Notification("Tye", "Tye installation failed", "", NotificationType.ERROR)
            .notify(project)
    }
}
