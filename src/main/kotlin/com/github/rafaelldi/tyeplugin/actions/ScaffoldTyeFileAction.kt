package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.github.rafaelldi.tyeplugin.tool.tyeInit
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.io.FileUtil
import java.nio.file.Paths

class ScaffoldTyeFileAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val task = object : Task.Backgroundable(e.project, "Scaffold tye file") {
            override fun run(indicator: ProgressIndicator) {
                scaffoldTyeFile(e.project!!, indicator)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun scaffoldTyeFile(project: Project, indicator: ProgressIndicator) {
        if (indicator.isCanceled) {
            return
        }

        indicator.isIndeterminate = true
        indicator.text = "Scaffolding tye.yaml file"

        val settings = TyeSettingsState.getInstance(project)
        val tyeToolPath = settings.tyeToolPath
        val pathToTyeFile = Paths.get(project.basePath!!, "tye.yaml")

        if (tyeToolPath == null) {
            indicator.cancel()
            Notification(
                "tye.notifications.balloon",
                "Could not find tye tool",
                "Please install tye global tool or specify the path to it.",
                NotificationType.ERROR
            )
                .addAction(InstallTyeGlobalToolNotificationAction("Install tye tool"))
                .notify(project)
        } else if (!settings.overwriteTyeFile && FileUtil.exists(pathToTyeFile.toString())) {
            indicator.cancel()
            Notification(
                "tye.notifications.balloon",
                "File tye.yaml already exists",
                "Please remove the file or allow rewriting in the settings.",
                NotificationType.WARNING
            ).notify(project)
        } else {
            tyeInit(tyeToolPath, project.basePath!!, settings.overwriteTyeFile)

            Notification(
                "tye.notifications.toolWindow",
                "File tye.yaml is scaffolded",
                "",
                NotificationType.INFORMATION
            ).notify(project)
        }
    }
}
