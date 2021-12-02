package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.settings.TyeSettings
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.EnvironmentUtil
import java.io.File

@Service
class TyePathProvider(private val project: Project) {
    companion object {
        const val TYE_TOOL = "tye"

        private const val WINDOWS_HOME_VARIABLE = "USERPROFILE"
        private const val LINUX_HOME_VARIABLE = "HOME"

        fun getDefaultGlobalPath(): String {
            val homeFolder =
                if (SystemInfo.isWindows) EnvironmentUtil.getValue(WINDOWS_HOME_VARIABLE)
                else EnvironmentUtil.getValue(LINUX_HOME_VARIABLE)

            return if (SystemInfo.isWindows) "$homeFolder\\.dotnet\\tools\\tye.exe"
            else "$homeFolder/.dotnet/tools/tye"
        }
    }

    fun getPath(): String? {
        val customPath = TyeSettings.getInstance(project).tyeToolPath

        if (!customPath.isNullOrEmpty() && isToolExists(customPath))
            return customPath

        val defaultPath = getDefaultGlobalPath()
        return if (isToolExists(defaultPath)) defaultPath else null
    }

    private fun isToolExists(path: String): Boolean {
        val file = File(path)
        return file.exists() && file.canExecute()
    }
}