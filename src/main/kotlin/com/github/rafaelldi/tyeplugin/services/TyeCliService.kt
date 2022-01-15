package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.util.TYE_FILE_NAME
import com.github.rafaelldi.tyeplugin.util.ToolVersion
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
    private val log = Logger.getInstance(TyeCliService::class.java)

    fun getVersion(path: String?): ToolVersion? {
        val tyePathProvider = project.service<TyePathProvider>()
        val tyePath = path ?: tyePathProvider.getPath() ?: return null

        val tyeCliClient = service<TyeCliClient>()
        val output = tyeCliClient.version(tyePath)

        return if (output.checkSuccess(log)) {
            ToolVersion(output.stdout)
        } else {
            log.error(output.stderr)
            null
        }
    }

    fun scaffoldTyeFile(overwriteFile: Boolean) {
        val tyePathProvider = project.service<TyePathProvider>()
        val tyePath = tyePathProvider.getPath() ?: return

        val options = TyeCliClient.InitOptions(project.basePath!!, project.basePath, overwriteFile)

        val tyeCliClient = service<TyeCliClient>()
        val output = tyeCliClient.init(tyePath, options)

        if (output.checkSuccess(log)) {
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