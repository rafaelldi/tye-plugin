package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.actions.InstallTyeGlobalToolNotificationAction
import com.github.rafaelldi.tyeplugin.util.ToolVersion
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessOutput
import com.intellij.execution.util.ExecUtil
import com.intellij.ide.BrowserUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationAction
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

@Service
class TyeGlobalToolService(private val project: Project) {
    companion object {
        const val TYE_ACTUAL_VERSION = "0.8.0-alpha.21352.1"
    }

    private val tyeGlobalToolPathProvider: TyeGlobalToolPathProvider = project.service()
    private val log = Logger.getInstance(TyeGlobalToolService::class.java)
    private val tyeActualVersion = ToolVersion(TYE_ACTUAL_VERSION)

    fun installTyeGlobalTool() {
        val isDotNetInstalled = isDotNetInstalled()
        if (!isDotNetInstalled) {
            Notification("Tye", ".NET Core 3.1 is not installed", "", NotificationType.ERROR)
                .addAction(object : NotificationAction("Go to .NET Core installation page") {
                    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                        BrowserUtil.browse("https://dotnet.microsoft.com/download/dotnet/3.1")
                    }
                })
                .notify(project)
            return
        }

        val isTyeGlobalToolInstalled = isTyeGlobalToolInstalled()
        if (isTyeGlobalToolInstalled) {
            Notification("Tye", "Tye is already installed", "", NotificationType.WARNING)
                .notify(project)
            return
        }

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("tool", "install", "Microsoft.Tye", "--global", "--version", TYE_ACTUAL_VERSION)
        val output = ExecUtil.execAndGetOutput(commandLine)
        val success = output.exitCode == 0

        if (success) {
            log.info("Tye is successfully installed with version $TYE_ACTUAL_VERSION")
            tyeGlobalToolPathProvider.refreshPath()

            Notification("Tye", "Tye is successfully installed", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            log.error(output.stderr)

            Notification("Tye", "Tye installation failed", output.stderr, NotificationType.ERROR)
                .addAction(object : NotificationAction("Go to troubleshooting page") {
                    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                        BrowserUtil.browse("https://aka.ms/failure-installing-tool")
                    }
                })
                .notify(project)
        }
    }

    fun updateTyeGlobalTool() {
        val isTyeGlobalToolInstalled = isTyeGlobalToolInstalled()
        if (!isTyeGlobalToolInstalled) {
            Notification("Tye", "Tye is not installed", "", NotificationType.WARNING)
                .addAction(InstallTyeGlobalToolNotificationAction())
                .notify(project)
            return
        }

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("tool", "update", "Microsoft.Tye", "--global", "--version", TYE_ACTUAL_VERSION)
        val output = ExecUtil.execAndGetOutput(commandLine)
        val success = output.exitCode == 0

        if (success) {
            log.info("Tye is successfully updated to version $TYE_ACTUAL_VERSION")

            Notification("Tye", "Tye is successfully updated", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            log.error(output.stderr)

            Notification("Tye", "Tye update failed", output.stderr, NotificationType.ERROR)
                .addAction(object : NotificationAction("Go to troubleshooting page") {
                    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                        BrowserUtil.browse("https://aka.ms/failure-installing-tool")
                    }
                })
                .notify(project)
        }
    }

    fun isActualTyeGlobalToolVersionInstalled(): Boolean {
        val isTyeGlobalToolInstalled = isTyeGlobalToolInstalled()
        if (!isTyeGlobalToolInstalled) return false

        val currentVersion = getTyeGlobalToolVersion() ?: return false

        return currentVersion >= tyeActualVersion
    }

    fun isTyeGlobalToolInstalled(): Boolean {
        val output = getListOfGlobalTools()

        if (output.exitCode != 0) {
            log.error(output.stderr)
            return false
        }

        val regex = Regex("^microsoft\\.tye", RegexOption.MULTILINE)
        return regex.containsMatchIn(output.stdout)
    }

    private fun isDotNetInstalled(): Boolean {
        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("--list-runtimes")
        val output = ExecUtil.execAndGetOutput(commandLine)

        if (output.exitCode != 0) {
            log.error(output.stderr)
            return false
        }

        val regex = Regex("^Microsoft\\.AspNetCore\\.App 3\\.1", RegexOption.MULTILINE)
        return regex.containsMatchIn(output.stdout)
    }

    private fun getTyeGlobalToolVersion(): ToolVersion? {
        val output = getListOfGlobalTools()

        if (output.exitCode != 0) {
            log.error(output.stderr)
            return null
        }

        val regex = Regex("^microsoft\\.tye\\s+([\\d.]+)", RegexOption.MULTILINE)
        val versionString = regex.find(output.stdout)?.groups?.get(1)?.value ?: return null

        return ToolVersion(versionString)
    }

    private fun getListOfGlobalTools(): ProcessOutput {
        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("tool", "list", "--global")
        return ExecUtil.execAndGetOutput(commandLine)
    }
}