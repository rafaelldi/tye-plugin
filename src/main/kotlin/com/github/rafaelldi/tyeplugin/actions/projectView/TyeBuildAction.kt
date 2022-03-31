package com.github.rafaelldi.tyeplugin.actions.projectView

import com.github.rafaelldi.tyeplugin.services.TyeCliService
import com.github.rafaelldi.tyeplugin.util.isDotNetSolutionFile
import com.github.rafaelldi.tyeplugin.util.isTyeFile
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.runBackgroundableTask

class TyeBuildAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return
        val tyeCliService = project.service<TyeCliService>()

        runBackgroundableTask("Tye build", e.project) {
            it.isIndeterminate = true
            it.text = "Tye build command is running"
            tyeCliService.runBuildCommand(virtualFile.path, project.basePath)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isVisible = isTyeOrDotNetFile(e)
    }

    private fun isTyeOrDotNetFile(e: AnActionEvent): Boolean {
        if (e.project == null) {
            return false
        }
        val virtualFile = e.getData(PlatformDataKeys.VIRTUAL_FILE) ?: return false
        return virtualFile.isTyeFile() || virtualFile.isDotNetSolutionFile()
    }
}