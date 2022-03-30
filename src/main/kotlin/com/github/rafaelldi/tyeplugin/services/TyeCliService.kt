package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.util.TYE_FILE_NAME
import com.github.rafaelldi.tyeplugin.util.ToolVersion
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import java.io.File
import java.nio.file.Paths

@Service
class TyeCliService(private val project: Project) {
    private val log = logger<TyeCliService>()

    fun getVersion(toolPath: String?): ToolVersion? {
        val tyePathProvider = project.service<TyePathProvider>()
        val tyePath = toolPath ?: tyePathProvider.getPath() ?: return null

        val tyeCliClient = service<TyeCliClient>()
        val output = tyeCliClient.version(tyePath)

        return if (output.checkSuccess(log)) {
            ToolVersion(output.stdout)
        } else {
            null
        }
    }

    fun scaffoldTyeFile(path: String, overwriteFile: Boolean) {
        val tyePathProvider = project.service<TyePathProvider>()
        val tyePath = tyePathProvider.getPath() ?: return

        val options = TyeCliClient.InitOptions(path, project.basePath, overwriteFile)

        val tyeCliClient = service<TyeCliClient>()
        val output = tyeCliClient.init(tyePath, options)

        if (output.checkSuccess(log)) {
            log.info("File tye.yaml is scaffolded")

            val directory = getDirectoryFromPath(path)
            VfsUtil.findFile(Paths.get(directory, TYE_FILE_NAME), true)?.refresh(true, false)

            Notification("Tye", "File tye.yaml is scaffolded", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            Notification("Tye", "Tye file scaffolding failed", output.stderr, NotificationType.ERROR)
                .notify(project)
        }
    }

    fun runBuildCommand(path: String, workDirectory: String?) {
        val tyePathProvider = project.service<TyePathProvider>()
        val tyePath = tyePathProvider.getPath() ?: return

        val options = TyeCliClient.BuildOptions(path, workDirectory)

        val tyeCliClient = service<TyeCliClient>()
        val output = tyeCliClient.build(tyePath, options)

        if (output.checkSuccess(log)) {
            Notification("Tye", "Tye build successfully completed", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            Notification("Tye", "Tye build failed", output.stderr, NotificationType.ERROR)
                .notify(project)
        }
    }

    fun runPushCommand(path: String, workDirectory: String?) {
        val tyePathProvider = project.service<TyePathProvider>()
        val tyePath = tyePathProvider.getPath() ?: return

        val options = TyeCliClient.PushOptions(path, workDirectory)

        val tyeCliClient = service<TyeCliClient>()
        val output = tyeCliClient.push(tyePath, options)

        if (output.checkSuccess(log)) {
            Notification("Tye", "Tye push successfully completed", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            Notification("Tye", "Tye push failed", output.stderr, NotificationType.ERROR)
                .notify(project)
        }
    }

    private fun getDirectoryFromPath(path: String): String {
        val file = File(path)
        return if (file.isDirectory) {
            path
        } else {
            file.parent
        }
    }
}