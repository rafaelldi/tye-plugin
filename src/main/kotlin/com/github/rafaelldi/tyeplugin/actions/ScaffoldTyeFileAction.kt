package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.services.TyeCliService
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.github.rafaelldi.tyeplugin.util.TYE_FILE_NAME
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.showOkCancelDialog
import com.intellij.openapi.util.io.FileUtil
import java.nio.file.Paths

class ScaffoldTyeFileAction : AnAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = e.project != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val settings = TyeSettingsState.getInstance(e.project!!)
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
                val tyeCliService = e.project!!.service<TyeCliService>()

                indicator.isIndeterminate = true
                indicator.text = "Scaffolding tye.yaml file"

                tyeCliService.scaffoldTyeFile(overwriteFile)
            }
        }
        ProgressManager.getInstance().run(task)
    }
}
