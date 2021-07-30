package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.EnvironmentUtil

@Service
class TyeGlobalToolPathProvider(private val project: Project) {
    companion object {
        fun findDefaultTyeGlobalToolPath(): String? {
            val homeFolder =
                if (SystemInfo.isWindows) EnvironmentUtil.getValue("USERPROFILE")
                else EnvironmentUtil.getValue("HOME")

            val tyePath =
                if (SystemInfo.isWindows) "$homeFolder\\.dotnet\\tools\\tye.exe"
                else "$homeFolder/.dotnet/tools/tye"

            if (!FileUtil.exists(tyePath)) return null

            return tyePath
        }
    }

    fun refreshPath(){
        TyeSettingsState.getInstance(project).tyeToolPath = findDefaultTyeGlobalToolPath()
    }

    fun getPath(): String? {
        val pathFromSettings = TyeSettingsState.getInstance(project).tyeToolPath
        if (!pathFromSettings.isNullOrBlank()) {
            return pathFromSettings
        }

        return findDefaultTyeGlobalToolPath()
    }
}