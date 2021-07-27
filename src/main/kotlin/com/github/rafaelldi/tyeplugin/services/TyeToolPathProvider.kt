package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project

@Service
class TyeToolPathProvider(private val project: Project) {
    fun getPath(): String? = TyeSettingsState.getInstance(project).tyeToolPath
}