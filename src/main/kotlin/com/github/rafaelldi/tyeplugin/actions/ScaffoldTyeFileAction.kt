package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.cli.TyeInitCliBuilder
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.github.rafaelldi.tyeplugin.util.TYE_FILE_NAME
import com.github.rafaelldi.tyeplugin.util.isTyeGlobalToolInstalled
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.showOkCancelDialog
import com.intellij.openapi.util.io.FileUtil
import java.nio.file.Paths

class ScaffoldTyeFileAction : AnAction() {
    private val log = Logger.getInstance(ScaffoldTyeFileAction::class.java)

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val settings = TyeSettingsState.getInstance(e.project!!)
        val pathToTyeFile = Paths.get(e.project!!.basePath!!, TYE_FILE_NAME)
        if (!settings.overwriteTyeFile && FileUtil.exists(pathToTyeFile.toString())) {
            val result = showOkCancelDialog(
                "Override?",
                "File tye.yaml already exists. Override it?",
                "Ok"
            )
            if (result != Messages.OK) return
        }

        val task = object : Task.Backgroundable(e.project, "Scaffold tye file") {
            override fun run(indicator: ProgressIndicator) {
                scaffoldTyeFile(e.project!!, settings, indicator)
            }
        }
        ProgressManager.getInstance().run(task)
    }

    private fun scaffoldTyeFile(project: Project, settings: TyeSettingsState, indicator: ProgressIndicator) {
        if (indicator.isCanceled) {
            return
        }

        indicator.isIndeterminate = true
        indicator.text = "Scaffolding tye.yaml file"

        if (!isTyeGlobalToolInstalled()) {
            Notification("Tye", "Tye is not installed", "", NotificationType.ERROR)
                .addAction(InstallTyeGlobalToolNotificationAction())
                .notify(project)
            return
        }

        val tyeToolPath = settings.tyeToolPath
        if (tyeToolPath == null) {
            Notification(
                "Tye",
                "Could not find tye global tool",
                "Please specify the path to it.",
                NotificationType.ERROR
            )
                .addAction(EditSettingsNotificationAction())
                .notify(project)
            return
        }

        if (runScaffoldCommand(tyeToolPath, settings.overwriteTyeFile, project.basePath!!)) {
            Notification("Tye", "File tye.yaml is scaffolded", "", NotificationType.INFORMATION)
                .notify(project)
        } else {
            Notification("Tye", "Tye file scaffolding failed", "", NotificationType.ERROR)
                .notify(project)
        }
    }

    private fun runScaffoldCommand(tyeToolPath: String, overwriteTyeFile: Boolean, projectPath: String): Boolean {
        val cliBuilder = TyeInitCliBuilder(projectPath)
        if (overwriteTyeFile) {
            cliBuilder.setForce()
        }
        val arguments = cliBuilder.build()

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(projectPath)
            .withExePath(tyeToolPath)
            .withParameters(arguments)

        val output = ExecUtil.execAndGetOutput(commandLine)

        if (output.exitCode != 0) log.error("Cannot scaffold tye file", output.stderr)

        return output.exitCode == 0
    }
}
