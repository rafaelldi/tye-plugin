package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.github.rafaelldi.tyeplugin.util.TYE_FILE_NAME
import com.github.rafaelldi.tyeplugin.util.isTyeGlobalToolInstalled
import com.intellij.execution.util.ExecUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.showOkCancelDialog
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VfsUtil
import java.nio.file.Paths

class ScaffoldTyeFileAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val settings = TyeSettingsState.getInstance(e.project!!)
        val tyeToolPath = settings.tyeToolPath
        if (tyeToolPath == null) {
            Notification(
                "Tye",
                "Could not find tye global tool",
                "Please specify the path to it.",
                NotificationType.ERROR
            )
                .addAction(EditSettingsNotificationAction())
                .notify(e.project)
            return
        }

        var overwriteFile = settings.overwriteTyeFile
        val pathToTyeFile = Paths.get(e.project!!.basePath!!, TYE_FILE_NAME)
        if (!overwriteFile && FileUtil.exists(pathToTyeFile.toString())) {
            val result = showOkCancelDialog(
                "Overwrite?",
                "File tye.yaml already exists. Overwrite it?",
                "Ok"
            )

            if (result == Messages.OK) overwriteFile = true
            else return
        }

        val task = object : Task.Backgroundable(e.project, "Scaffold tye file") {
            override fun run(indicator: ProgressIndicator) {
                if (indicator.isCanceled) {
                    return
                }

                if (!isTyeGlobalToolInstalled()) {
                    Notification("Tye", "Tye is not installed", "", NotificationType.ERROR)
                        .addAction(InstallTyeGlobalToolNotificationAction())
                        .notify(e.project)
                    return
                }

                indicator.isIndeterminate = true
                indicator.text = "Scaffolding tye.yaml file"

                scaffoldTyeFile(e.project!!, tyeToolPath, overwriteFile)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun scaffoldTyeFile(project: Project, tyeToolPath: String, overwriteFile: Boolean) {
        if (runScaffoldCommand(tyeToolPath, overwriteFile, project.basePath!!)) {
            Notification("Tye", "File tye.yaml is scaffolded", "", NotificationType.INFORMATION)
                .notify(project)

            VfsUtil.findFile(Paths.get(project.basePath!!, TYE_FILE_NAME), true)
        } else {
            Notification("Tye", "Tye file scaffolding failed", "", NotificationType.ERROR)
                .notify(project)
        }
    }

    private fun runScaffoldCommand(tyeToolPath: String, overwriteTyeFile: Boolean, projectPath: String): Boolean {
        val tyeCliClient = TyeCliClient(tyeToolPath, projectPath)
        val options = TyeCliClient.InitOptions(overwriteTyeFile)
        val commandLine = tyeCliClient.init(projectPath, options)
        val output = ExecUtil.execAndGetOutput(commandLine)

        return output.exitCode == 0
    }
}
