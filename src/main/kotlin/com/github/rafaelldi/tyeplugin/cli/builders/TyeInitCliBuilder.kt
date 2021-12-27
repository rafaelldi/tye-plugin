package com.github.rafaelldi.tyeplugin.cli.builders

import com.intellij.execution.configurations.GeneralCommandLine

class TyeInitCliBuilder(private val tyeCliPath: String, private val workDirectory: String?) {
    private var pathArgument: String? = null
    private val arguments: MutableList<String> = mutableListOf()

    init {
        arguments.add("init")
    }

    fun setPath(path: String) {
        pathArgument = path
    }

    fun setForce() {
        arguments.add("--force")
    }

    fun build(): GeneralCommandLine {
        if (pathArgument.isNullOrEmpty())
            throw IllegalArgumentException("Path argument cannot be null or empty")

        arguments.add(pathArgument!!)

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(workDirectory)
            .withExePath(tyeCliPath)
            .withParameters(arguments)
    }
}
