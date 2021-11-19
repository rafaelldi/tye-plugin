package com.github.rafaelldi.tyeplugin.listeners

import com.github.rafaelldi.tyeplugin.services.TyeCliService
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project

class TyeSettingsGlobalToolListener(private val project: Project) : TyeGlobalToolListener {
    private val tyeCliService: TyeCliService = project.service()

    override fun tyeToolPathChanged(path: String) {
        val task = object : Task.Backgroundable(project, "Get tye tool version") {
            override fun run(indicator: ProgressIndicator) {
                TyeSettingsState.getInstance(project).tyeToolVersion = tyeCliService.getVersion() ?: ""
            }
        }
        ProgressManager.getInstance().run(task)
    }
}