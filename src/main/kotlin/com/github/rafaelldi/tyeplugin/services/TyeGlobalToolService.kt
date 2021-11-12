package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.actions.InstallTyeGlobalToolNotificationAction
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
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
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.getComponent
import com.jetbrains.rider.runtime.RiderDotNetActiveRuntimeHost

@Service
class TyeGlobalToolService(private val project: Project) {
    companion object {
        private const val TYE_ACTUAL_VERSION = "0.10.0-alpha.21420.1"
    }

    private val activeRuntimeHost = project.getComponent<RiderDotNetActiveRuntimeHost>()
    private val log = Logger.getInstance(TyeGlobalToolService::class.java)
    private val tyeActualVersion = ToolVersion(TYE_ACTUAL_VERSION)

    fun installTyeGlobalTool() {
        val dotnetPath = getDotnetPath() ?: return

        val isDotNetInstalled = isDotNetRuntime31Installed(dotnetPath)
        if (!isDotNetInstalled) {
            Notification("Tye", ".NET Core 3.1 Runtime is not installed", "", NotificationType.ERROR)
                .addAction(object : NotificationAction("Go to .NET Core installation page") {
                    override fun actionPerformed(e: AnActionEvent, notification: Notification) {
                        BrowserUtil.browse("https://dotnet.microsoft.com/download/dotnet/3.1")
                    }
                })
                .notify(project)
            return
        }

        val isTyeGlobalToolInstalled = isTyeGlobalToolInstalled(dotnetPath)
        if (isTyeGlobalToolInstalled) {
            Notification("Tye", "Tye is already installed", "", NotificationType.INFORMATION)
                .notify(project)
            return
        }

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath(dotnetPath)
            .withParameters("tool", "install", "Microsoft.Tye", "--global", "--version", TYE_ACTUAL_VERSION)
        val output = ExecUtil.execAndGetOutput(commandLine)
        val success = output.exitCode == 0

        if (success) {
            log.info("Tye is successfully installed with version $TYE_ACTUAL_VERSION")
            TyeSettingsState.getInstance(project).tyeToolPath = TyePathProvider.getDefaultGlobalPath()

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
        val dotnetPath = getDotnetPath() ?: return

        val isTyeGlobalToolInstalled = isTyeGlobalToolInstalled(dotnetPath)
        if (!isTyeGlobalToolInstalled) {
            Notification("Tye", "Tye is not installed", "", NotificationType.WARNING)
                .addAction(InstallTyeGlobalToolNotificationAction())
                .notify(project)
            return
        }

        val isActualVersionInstalled = isActualTyeGlobalToolVersionInstalled()
        if (isActualVersionInstalled) {
            Notification("Tye", "The actual version is already installed ", "", NotificationType.INFORMATION)
                .notify(project)
            return
        }

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath(dotnetPath)
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
        val dotnetPath = getDotnetPath() ?: return false

        val isTyeGlobalToolInstalled = isTyeGlobalToolInstalled(dotnetPath)
        if (!isTyeGlobalToolInstalled) return false

        val currentVersion = getTyeGlobalToolVersion(dotnetPath) ?: return false

        return currentVersion >= tyeActualVersion
    }

    private fun getDotnetPath(): String? = activeRuntimeHost.dotNetCoreRuntime.value?.cliExePath

    private fun isDotNetRuntime31Installed(dotnetPath: String): Boolean {
        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath(dotnetPath)
            .withParameters("--list-runtimes")
        val output = ExecUtil.execAndGetOutput(commandLine)

        if (output.exitCode != 0) {
            log.error(output.stderr)
            return false
        }

        val regex = Regex("^Microsoft\\.AspNetCore\\.App 3\\.1", RegexOption.MULTILINE)
        return regex.containsMatchIn(output.stdout)
    }

    private fun isTyeGlobalToolInstalled(dotnetPath: String): Boolean {
        val output = getListOfGlobalTools(dotnetPath)

        if (output.exitCode != 0) {
            log.error(output.stderr)
            return false
        }

        val regex = Regex("^microsoft\\.tye", RegexOption.MULTILINE)
        return regex.containsMatchIn(output.stdout)
    }

    private fun getTyeGlobalToolVersion(dotnetPath: String): ToolVersion? {
        val output = getListOfGlobalTools(dotnetPath)

        if (output.exitCode != 0) {
            log.error(output.stderr)
            return null
        }

        val regex = Regex("^microsoft\\.tye\\s+([\\d.]+)", RegexOption.MULTILINE)
        val versionString = regex.find(output.stdout)?.groups?.get(1)?.value ?: return null

        return ToolVersion(versionString)
    }

    private fun getListOfGlobalTools(dotnetPath: String): ProcessOutput {
        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath(dotnetPath)
            .withParameters("tool", "list", "--global")

        return ExecUtil.execAndGetOutput(commandLine)
    }
}