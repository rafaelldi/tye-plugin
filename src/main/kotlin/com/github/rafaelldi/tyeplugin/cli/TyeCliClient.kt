package com.github.rafaelldi.tyeplugin.cli

import com.intellij.execution.configurations.GeneralCommandLine

class TyeCliClient(private val tyePath: String, private val projectPath: String?) {
    companion object {
        const val DEFAULT_PORT = 8000
        const val DEFAULT_VERBOSITY = "info"
        const val DEFAULT_LOGS_PROVIDER = "none"
        const val DEFAULT_TRACES_PROVIDER = "none"
    }

    data class InitOptions(val overwriteExistingFile: Boolean)

    data class RunOptions(
        val port: Int,
        val noBuild: Boolean,
        val docker: Boolean,
        val dashboard: Boolean,
        val verbosity: String,
        val tags: String?,
        val logsProvider: String,
        val logsProviderUrl: String?,
        val tracesProvider: String,
        val tracesProviderUrl: String?
    )

    fun init(path: String, options: InitOptions): GeneralCommandLine {
        val cliBuilder = TyeInitCliBuilder(path)

        if (options.overwriteExistingFile) cliBuilder.setForce()

        val arguments = cliBuilder.build()

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(projectPath)
            .withExePath(tyePath)
            .withParameters(arguments)
    }

    fun run(path: String, options: RunOptions): GeneralCommandLine {
        val cliBuilder = TyeRunCliBuilder(path)

        if (options.port != DEFAULT_PORT) cliBuilder.setPort(options.port)

        if (options.noBuild) cliBuilder.setNoBuild()

        if (options.docker) cliBuilder.setDocker()

        if (options.dashboard) cliBuilder.setDashboard()

        if (options.verbosity != DEFAULT_VERBOSITY) cliBuilder.setVerbosity(options.verbosity)

        if (!options.tags.isNullOrBlank()) cliBuilder.setTags(options.tags)

        if (options.logsProvider != DEFAULT_LOGS_PROVIDER) cliBuilder.setLogs(
            options.logsProvider,
            options.logsProviderUrl
        )

        if (options.tracesProvider != DEFAULT_TRACES_PROVIDER) cliBuilder.setLogs(
            options.tracesProvider,
            options.tracesProviderUrl
        )

        val arguments = cliBuilder.build()

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(projectPath)
            .withExePath(tyePath)
            .withParameters(arguments)
    }
}