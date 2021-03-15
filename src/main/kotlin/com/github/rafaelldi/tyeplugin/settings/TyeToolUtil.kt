package com.github.rafaelldi.tyeplugin.settings

import com.intellij.execution.ExecutionException
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.EnvironmentUtil

fun findTyeToolPath(): String {
    val homeFolder = EnvironmentUtil.getValue("HOME")
    val tyePath =
        if (SystemInfo.isWindows) "\${USERPROFILE}\\.dotnet\\tools\\tye.exe" else "$homeFolder/.dotnet/tools/tye"

    if (!FileUtil.exists(tyePath)) throw ExecutionException("Tye tool not found.")

    return tyePath
}
