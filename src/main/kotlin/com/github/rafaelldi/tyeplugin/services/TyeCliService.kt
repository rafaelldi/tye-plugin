package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.actions.InstallTyeGlobalToolNotificationAction
import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.util.TYE_FILE_NAME
import com.intellij.execution.util.ExecUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import java.nio.file.Paths

@Service
class TyeCliService(private val project: Project) {
    private val tyeCliClient: TyeCliClient = project.service()
    private val tyeGlobalToolService: TyeGlobalToolService = project.service()
    private val log = Logger.getInstance(TyeCliService::class.java)

    fun scaffoldTyeFile(overwriteFile: Boolean) {
        val isTyeGlobalToolInstalled = tyeGlobalToolService.isTyeGlobalToolInstalled()
        if (!isTyeGlobalToolInstalled) {
            Notification("Tye", "Tye is not installed", "", NotificationType.ERROR)
                .addAction(InstallTyeGlobalToolNotificationAction())
                .notify(project)
            return
        }

        val options = TyeCliClient.InitOptions(project.basePath!!, overwriteFile)
        val commandLine = tyeCliClient.init(options)
        val output = ExecUtil.execAndGetOutput(commandLine)
        val success = output.exitCode == 0

        if (success) {
            log.info("File tye.yaml is scaffolded")
            VfsUtil.findFile(Paths.get(project.basePath!!, TYE_FILE_NAME), true)

            Notification("Tye", "File tye.yaml is scaffolded", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            Notification("Tye", "Tye file scaffolding failed", "", NotificationType.ERROR)
                .notify(project)
        }
    }
}