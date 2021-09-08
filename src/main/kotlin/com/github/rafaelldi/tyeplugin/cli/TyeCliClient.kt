package com.github.rafaelldi.tyeplugin.cli

import com.github.rafaelldi.tyeplugin.cli.builders.TyeInitCliBuilder
import com.github.rafaelldi.tyeplugin.cli.builders.TyeRunCliBuilder
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger

@Service
class TyeCliClient {
    companion object {
        private const val DEFAULT_PORT = 8000
        private const val DEFAULT_VERBOSITY = "info"
        private const val DEFAULT_LOGS_PROVIDER = "none"
        private const val DEFAULT_TRACES_PROVIDER = "none"
    }

    data class InitOptions(val path: String, val workDirectory: String?, val overwriteExistingFile: Boolean)

    data class RunOptions(
        val path: String,
        val workDirectory: String?,
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

    private val log = Logger.getInstance(TyeCliClient::class.java)

    fun version(tyePath: String): GeneralCommandLine {
        log.info("Call version command")

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath(tyePath)
            .withParameters("--version")
    }

    fun init(tyePath: String, options: InitOptions): GeneralCommandLine {
        val cliBuilder = TyeInitCliBuilder(options.path)

        if (options.overwriteExistingFile) cliBuilder.setForce()

        val arguments = cliBuilder.build()

        log.info("Call init command with arguments $arguments")

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(options.workDirectory)
            .withExePath(tyePath)
            .withParameters(arguments)
    }

    fun run(tyePath: String, options: RunOptions): GeneralCommandLine {
        val cliBuilder = TyeRunCliBuilder(options.path)

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

        if (options.tracesProvider != DEFAULT_TRACES_PROVIDER) cliBuilder.setTraces(
            options.tracesProvider,
            options.tracesProviderUrl
        )

        val arguments = cliBuilder.build()

        log.info("Call run command with arguments $arguments")

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(options.workDirectory)
            .withExePath(tyePath)
            .withParameters(arguments)
    }
}