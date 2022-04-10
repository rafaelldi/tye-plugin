package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.settings.TyeSettings
import com.github.rafaelldi.tyeplugin.util.isTyeExecutable
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.EnvironmentUtil
import java.io.File

@Service
class TyePathProvider(private val project: Project) {
    companion object {
        private const val WINDOWS_HOME_VARIABLE = "USERPROFILE"
        private const val LINUX_HOME_VARIABLE = "HOME"

        fun getDefaultGlobalPath(): String {
            val homeFolder =
                if (SystemInfo.isWindows) EnvironmentUtil.getValue(WINDOWS_HOME_VARIABLE)
                else EnvironmentUtil.getValue(LINUX_HOME_VARIABLE)

            return if (SystemInfo.isWindows)
                "$homeFolder\\.dotnet\\tools\\tye.exe"
            else
                "$homeFolder/.dotnet/tools/tye"
        }

        fun isValidTyeToolPath(path: String): Boolean {
            val file = File(path)
            return file.isTyeExecutable()
        }
    }

    fun getPath(): String? {
        val customPath = TyeSettings.getInstance(project).tyeToolPath

        if (customPath.isNotEmpty() && isValidTyeToolPath(customPath))
            return customPath

        val defaultPath = getDefaultGlobalPath()
        return if (isValidTyeToolPath(defaultPath)) defaultPath else null
    }
}