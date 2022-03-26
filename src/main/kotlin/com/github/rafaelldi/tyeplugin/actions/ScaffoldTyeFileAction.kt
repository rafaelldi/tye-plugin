package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeCliService
import com.github.rafaelldi.tyeplugin.settings.TyeSettings
import com.github.rafaelldi.tyeplugin.util.TYE_FILE_NAME
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.ui.showYesNoDialog
import com.intellij.util.io.exists
import com.jetbrains.rider.projectView.solutionDirectoryPath
import com.jetbrains.rider.projectView.solutionPath
import java.nio.file.Paths

class ScaffoldTyeFileAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return

        val settings = TyeSettings.getInstance(project)
        var overwriteFile = settings.overwriteTyeFile
        val pathToTyeFile = Paths.get(project.solutionDirectoryPath.toString(), TYE_FILE_NAME)

        if (!overwriteFile && pathToTyeFile.exists()) {
            val result = showYesNoDialog(
                "Overwrite?",
                "File tye.yaml already exists. Overwrite it?",
                project
            )

            if (result) overwriteFile = true
            else return
        }

        val tyeCliService = project.service<TyeCliService>()
        runBackgroundableTask("Scaffold tye file", project) {
            it.isIndeterminate = true
            it.text = "Scaffolding tye.yaml file"

            tyeCliService.scaffoldTyeFile(project.solutionPath, overwriteFile)
        }
    }
}
