package com.github.rafaelldi.tyeplugin.cli.builders

import com.intellij.execution.configurations.GeneralCommandLine

class TyeBuildCliBuilder(private val tyeCliPath: String, private val workDirectory: String?) {
    private var pathArgument: String? = null
    private val arguments: MutableList<String> = mutableListOf()

    init {
        arguments.add("build")
    }

    fun setPath(path: String) {
        pathArgument = path
    }

    fun build(): GeneralCommandLine {
        val path = pathArgument
        if (path.isNullOrEmpty())
            throw IllegalArgumentException("Path argument cannot be null or empty")

        arguments.add(path)

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(workDirectory)
            .withExePath(tyeCliPath)
            .withParameters(arguments)
    }
}