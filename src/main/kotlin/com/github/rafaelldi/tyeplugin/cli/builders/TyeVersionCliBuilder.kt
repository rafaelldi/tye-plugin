package com.github.rafaelldi.tyeplugin.cli.builders

import com.intellij.execution.configurations.GeneralCommandLine

class TyeVersionCliBuilder(private val tyeCliPath: String) {
    fun build(): GeneralCommandLine = GeneralCommandLine()
        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
        .withExePath(tyeCliPath)
        .withParameters("--version")
}