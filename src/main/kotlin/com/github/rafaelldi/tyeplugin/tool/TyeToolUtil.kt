package com.github.rafaelldi.tyeplugin.tool

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.EnvironmentUtil

fun dotnetToolInstallTye(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("tool", "install", "Microsoft.Tye", "--global", "--version", "0.6.0-alpha.21070.5")
    val output = ExecUtil.execAndGetOutput(commandLine)
    return output.exitCode == 0
}

fun findTyeToolPath(): String? {
    val homeFolder = EnvironmentUtil.getValue("HOME")
    val tyePath =
        if (SystemInfo.isWindows) "\${USERPROFILE}\\.dotnet\\tools\\tye.exe" else "$homeFolder/.dotnet/tools/tye"

    if (!FileUtil.exists(tyePath)) return null

    return tyePath
}

fun tyeInit(tyeToolPath: String, solutionFolder: String): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withWorkDirectory(solutionFolder)
        .withExePath(tyeToolPath)
        .withParameters("init")
    val output = ExecUtil.execAndGetOutput(commandLine)
    return output.exitCode == 0
}
