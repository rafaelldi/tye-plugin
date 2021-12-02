package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.util.TYE_FILE_NAME
import com.github.rafaelldi.tyeplugin.util.ToolVersion
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
    private val tyeCliClient: TyeCliClient = service()
    private val tyePathProvider: TyePathProvider = project.service()
    private val log = Logger.getInstance(TyeCliService::class.java)

    fun getVersion(path: String?): ToolVersion? {
        val tyePath = path ?: tyePathProvider.getPath() ?: return null

        val commandLine = tyeCliClient.version(tyePath)
        val output = ExecUtil.execAndGetOutput(commandLine)
        val success = output.exitCode == 0

        if (!success) {
            log.error(output.stderr)
            return null
        }

        return ToolVersion(output.stdout)
    }

    fun scaffoldTyeFile(overwriteFile: Boolean) {
        val tyePath = tyePathProvider.getPath() ?: return

        val options = TyeCliClient.InitOptions(project.basePath!!, project.basePath, overwriteFile)
        val commandLine = tyeCliClient.init(tyePath, options)
        val output = ExecUtil.execAndGetOutput(commandLine)
        val success = output.exitCode == 0

        if (success) {
            log.info("File tye.yaml is scaffolded")
            VfsUtil.findFile(Paths.get(project.basePath!!, TYE_FILE_NAME), true)

            Notification("Tye", "File tye.yaml is scaffolded", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            log.error(output.stderr)

            Notification("Tye", "Tye file scaffolding failed", output.stderr, NotificationType.ERROR)
                .notify(project)
        }
    }
}