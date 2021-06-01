package com.github.rafaelldi.tyeplugin.util

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.EnvironmentUtil

fun isDotNetInstalled(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("--list-runtimes")
    val output = ExecUtil.execAndGetOutput(commandLine)

    if (output.exitCode != 0) return false

    val regex = Regex("^Microsoft\\.AspNetCore\\.App 3\\.1", RegexOption.MULTILINE)
    return regex.containsMatchIn(output.stdout)
}

fun isTyeGlobalToolInstalled(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("tool", "list", "--global")
    val output = ExecUtil.execAndGetOutput(commandLine)

    if (output.exitCode != 0) return false

    val regex = Regex("^microsoft\\.tye", RegexOption.MULTILINE)
    return regex.containsMatchIn(output.stdout)
}

fun installTyeGlobalTool(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("tool", "install", "Microsoft.Tye", "--global", "--version", "0.6.0-alpha.21070.5")
    val output = ExecUtil.execAndGetOutput(commandLine)
    return output.exitCode == 0
}

fun findTyeGlobalToolPath(): String? {
    val homeFolder =
        if (SystemInfo.isWindows) EnvironmentUtil.getValue("USERPROFILE")
        else EnvironmentUtil.getValue("HOME")

    val tyePath =
        if (SystemInfo.isWindows) "$homeFolder\\.dotnet\\tools\\tye.exe"
        else "$homeFolder/.dotnet/tools/tye"

    if (!FileUtil.exists(tyePath)) return null

    return tyePath
}
