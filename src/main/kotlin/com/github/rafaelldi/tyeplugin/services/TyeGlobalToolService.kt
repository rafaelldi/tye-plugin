package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.actions.notification.InstallTyeGlobalToolAction
import com.github.rafaelldi.tyeplugin.settings.TyeSettings
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
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project

@Service
class TyeGlobalToolService(private val project: Project) {
    companion object {
        private const val TYE_ACTUAL_VERSION = "0.11.0-alpha.22111.1"
    }

    private val log = logger<TyeGlobalToolService>()
    private val tyeActualVersion = ToolVersion(TYE_ACTUAL_VERSION)

    fun installTyeGlobalTool() {
        val isDotNet6Installed = isDotNet6Installed()
        if (!isDotNet6Installed) {
            Notification("Tye", ".NET 6 is not installed", "", NotificationType.WARNING)
                .addAction(object : NotificationAction("Go to .NET installation page") {
                    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                        BrowserUtil.browse("https://dotnet.microsoft.com/download/dotnet/6.0")
                    }
                })
                .notify(project)
            return
        }

        val isTyeGlobalToolInstalled = isTyeGlobalToolInstalled()
        if (isTyeGlobalToolInstalled) {
            Notification("Tye", "Tye is already installed", "", NotificationType.INFORMATION)
                .notify(project)
            return
        }

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("tool", "install", "Microsoft.Tye", "--global", "--version", TYE_ACTUAL_VERSION)
        val output = ExecUtil.execAndGetOutput(commandLine)

        if (output.checkSuccess(log)) {
            log.info("Tye is successfully installed with version $TYE_ACTUAL_VERSION")
            TyeSettings.getInstance(project).tyeToolPath = TyePathProvider.getDefaultGlobalPath()

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
                .addAction(InstallTyeGlobalToolAction())
                .notify(project)
            return
        }

        val isActualVersionInstalled = isActualTyeGlobalToolVersionInstalled()
        if (isActualVersionInstalled) {
            Notification("Tye", "The actual version is already installed ", "", NotificationType.INFORMATION)
                .notify(project)
            return
        }

        val isDotNet6Installed = isDotNet6Installed()
        if (!isDotNet6Installed) {
            Notification("Tye", "To update tye you need to install .NET 6", "", NotificationType.WARNING)
                .addAction(object : NotificationAction("Go to .NET installation page") {
                    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                        BrowserUtil.browse("https://dotnet.microsoft.com/download/dotnet/6.0")
                    }
                })
                .notify(project)
            return
        }

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("tool", "update", "Microsoft.Tye", "--global", "--version", TYE_ACTUAL_VERSION)
        val output = ExecUtil.execAndGetOutput(commandLine)

        if (output.checkSuccess(log)) {
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

    fun uninstallTyeGlobalTool() {
        val isTyeGlobalToolInstalled = isTyeGlobalToolInstalled()
        if (!isTyeGlobalToolInstalled) {
            Notification("Tye", "Tye is not installed", "", NotificationType.WARNING)
                .addAction(InstallTyeGlobalToolAction())
                .notify(project)
            return
        }

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("tool", "uninstall", "Microsoft.Tye", "--global")
        val output = ExecUtil.execAndGetOutput(commandLine)

        if (output.checkSuccess(log)) {
            log.info("Tye is successfully uninstalled")

            Notification("Tye", "Tye is successfully uninstalled", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            log.error(output.stderr)

            Notification("Tye", "Tye uninstallation failed", output.stderr, NotificationType.ERROR)
                .notify(project)
        }
    }

    fun isTyeGlobalToolInstalled(): Boolean {
        val output = getListOfGlobalTools()

        return if (output.checkSuccess(log)) {
            val regex = Regex("^microsoft\\.tye", RegexOption.MULTILINE)
            regex.containsMatchIn(output.stdout)
        } else {
            log.error(output.stderr)
            false
        }
    }

    fun isActualTyeGlobalToolVersionInstalled(): Boolean {
        val currentVersion = getTyeGlobalToolVersion() ?: return false

        return currentVersion >= tyeActualVersion
    }

    private fun isDotNet6Installed(): Boolean {
        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("--list-runtimes")
        val output = ExecUtil.execAndGetOutput(commandLine)

        return if (output.checkSuccess(log)) {
            val regex = Regex("^Microsoft\\.AspNetCore\\.App 6", RegexOption.MULTILINE)
            regex.containsMatchIn(output.stdout)
        } else {
            log.error(output.stderr)
            false
        }
    }

    private fun getTyeGlobalToolVersion(): ToolVersion? {
        val output = getListOfGlobalTools()

        if (output.checkSuccess(log)) {
            val regex = Regex("^microsoft\\.tye\\s+([\\d.]+)", RegexOption.MULTILINE)
            val versionString = regex.find(output.stdout)?.groups?.get(1)?.value ?: return null

            return ToolVersion(versionString)
        } else {
            log.error(output.stderr)
            return null
        }
    }

    private fun getListOfGlobalTools(): ProcessOutput {
        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath("dotnet")
            .withParameters("tool", "list", "--global")

        return ExecUtil.execAndGetOutput(commandLine)
    }
}