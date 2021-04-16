package com.github.rafaelldi.tyeplugin.util

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil
import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.EnvironmentUtil

fun installGlobalTool(project: Project) {
    if (!isDotnetInstalled()) {
        Notification(
            "tye.notifications.balloon",
            ".NET Core 3.1 is not installed",
            "",
            NotificationType.ERROR
        )
            .addAction(object : NotificationAction("Go to .NET Core installation page") {
                override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                    BrowserUtil.browse("https://dotnet.microsoft.com/download/dotnet/3.1")
                }
            })
            .notify(project)
    }

    if (isTyeGlobalToolInstalled()) {
        Notification(
            "tye.notifications.balloon",
            "Tye is already installed",
            "",
            NotificationType.WARNING
        ).notify(project)
        return
    }

    if (dotnetToolInstallTye()) {
        TyeSettingsState.getInstance(project).tyeToolPath = findTyeGlobalToolPath()
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

fun isDotnetInstalled(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("--list-runtimes")
    val output = ExecUtil.execAndGetOutput(commandLine)

    if (output.exitCode != 0) return false

    val regex = Regex("^Microsoft\\.AspNetCore\\.App 3\\.1", RegexOption.MULTILINE)
    return regex.containsMatchIn(output.stdout)
}

fun dotnetToolInstallTye(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("tool", "install", "Microsoft.Tye", "--global", "--version", "0.6.0-alpha.21070.5")
    val output = ExecUtil.execAndGetOutput(commandLine)
    return output.exitCode == 0
}

fun isTyeGlobalToolInstalled(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("tool", "list", "--global")
    val output = ExecUtil.execAndGetOutput(commandLine)

    if (output.exitCode != 0) return false

    val regex = Regex("^microsoft\\.tye", RegexOption.MULTILINE)
    return regex.containsMatchIn(output.stdout)
}

fun findTyeGlobalToolPath(): String? {
    val homeFolder =
        if (SystemInfo.isWindows) EnvironmentUtil.getValue("USERPROFILE")
        else EnvironmentUtil.getValue("HOME")

    val tyePath =
        if (SystemInfo.isWindows) "$homeFolder\\.dotnet\\tools\\tye.exe"
        else "$homeFolder/.dotnet/tools/tye"

    if (!FileUtil.exists(tyePath)) return null

    return tyePath
}
