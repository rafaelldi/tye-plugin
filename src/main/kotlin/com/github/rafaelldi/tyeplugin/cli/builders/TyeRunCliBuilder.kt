package com.github.rafaelldi.tyeplugin.cli.builders

import com.intellij.execution.configurations.GeneralCommandLine

class TyeRunCliBuilder(private val tyeCliPath: String, private val workDirectory: String?) {
    private var pathArgument: String? = null
    private val arguments: MutableList<String> = mutableListOf()

    init {
        arguments.add("run")
    }

    fun setPath(path: String) {
        pathArgument = path
    }

    fun setPort(port: Int) {
        arguments.add("--port")
        arguments.add(port.toString())
    }

    fun setNoBuild() {
        arguments.add("--no-build")
    }

    fun setDocker() {
        arguments.add("--docker")
    }

    fun setDashboard() {
        arguments.add("--dashboard")
    }

    fun setWatch() {
        arguments.add("--watch")
    }

    fun setVerbosity(verbosity: String) {
        arguments.add("--verbosity")
        arguments.add(verbosity)
    }

    fun setTags(tags: String) {
        arguments.add("--tags")
        arguments.add(tags)
    }

    fun setDebug(debug: String) {
        arguments.add("--debug")
        arguments.add(debug)
    }

    fun setLogs(provider: String, url: String?) {
        arguments.add("--logs")
        var providerArg = provider
        if (!url.isNullOrBlank()) providerArg += "=$url"
        arguments.add(providerArg)
    }

    fun setTraces(provider: String, url: String?) {
        arguments.add("--dtrace")
        var providerArg = provider
        if (!url.isNullOrBlank()) providerArg += "=$url"
        arguments.add(providerArg)
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
