package com.github.rafaelldi.tyeplugin.util

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.ProcessOutput
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.EnvironmentUtil

const val TYE_ACTUAL_VERSION = "0.8.0-alpha.21352.1"

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
    val output = getListOfGlobalTools()

    if (output.exitCode != 0) return false

    val regex = Regex("^microsoft\\.tye", RegexOption.MULTILINE)
    return regex.containsMatchIn(output.stdout)
}

fun getTyeGlobalToolVersion(): ToolVersion? {
    val output = getListOfGlobalTools()

    if (output.exitCode != 0) return null

    val regex = Regex("^microsoft\\.tye\\s+([\\d.]+)", RegexOption.MULTILINE)
    val versionString = regex.find(output.stdout)?.groups?.get(1)?.value ?: return null

    return ToolVersion(versionString)
}

private fun getListOfGlobalTools(): ProcessOutput {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("tool", "list", "--global")
    return ExecUtil.execAndGetOutput(commandLine)
}

fun installTyeGlobalTool(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("tool", "install", "Microsoft.Tye", "--global", "--version", TYE_ACTUAL_VERSION)
    val output = ExecUtil.execAndGetOutput(commandLine)
    return output.exitCode == 0
}

fun updateTyeGlobalTool(): Boolean {
    val commandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath("dotnet")
        .withParameters("tool", "update", "Microsoft.Tye", "--global", "--version", TYE_ACTUAL_VERSION)
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
