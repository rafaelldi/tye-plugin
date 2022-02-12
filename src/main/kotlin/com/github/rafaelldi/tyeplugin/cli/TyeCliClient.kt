package com.github.rafaelldi.tyeplugin.cli

import com.github.rafaelldi.tyeplugin.cli.builders.TyeInitCliBuilder
import com.github.rafaelldi.tyeplugin.cli.builders.TyeRunCliBuilder
import com.github.rafaelldi.tyeplugin.cli.builders.TyeVersionCliBuilder
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessOutput
import com.intellij.execution.util.ExecUtil
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.logger

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

    private val log = logger<TyeCliClient>()

    fun version(tyePath: String): ProcessOutput {
        log.info("Call version command")

        val cliBuilder = TyeVersionCliBuilder(tyePath)

        val commandLine = cliBuilder.build()
        return ExecUtil.execAndGetOutput(commandLine)
    }

    fun init(tyePath: String, options: InitOptions): ProcessOutput {
        log.info("Call init command")

        val cliBuilder = TyeInitCliBuilder(tyePath, options.workDirectory)

        cliBuilder.setPath(options.path)
        if (options.overwriteExistingFile) cliBuilder.setForce()

        val commandLine = cliBuilder.build()
        return ExecUtil.execAndGetOutput(commandLine)
    }

    fun run(tyePath: String, options: RunOptions): ColoredProcessHandler {
        log.info("Call run command")

        val cliBuilder = TyeRunCliBuilder(tyePath, options.workDirectory)

        cliBuilder.setPath(options.path)
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

        val commandLine = cliBuilder.build()
        return KillableColoredProcessHandler.Silent(commandLine)
    }
}