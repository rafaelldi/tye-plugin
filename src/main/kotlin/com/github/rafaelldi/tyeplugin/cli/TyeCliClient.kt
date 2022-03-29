package com.github.rafaelldi.tyeplugin.cli

import com.github.rafaelldi.tyeplugin.cli.builders.*
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
    data class BuildOptions(val path: String, val workDirectory: String?)
    data class PushOptions(val path: String, val workDirectory: String?)
    data class RunOptions(
        val path: String,
        val workDirectory: String?,
        val port: Int,
        val noBuild: Boolean,
        val docker: Boolean,
        val dashboard: Boolean,
        val watch: Boolean,
        val verbosity: String,
        val tags: String?,
        val debug: String?,
        val framework: String?,
        val logsProvider: String,
        val logsProviderUrl: String?,
        val tracesProvider: String,
        val tracesProviderUrl: String?
    )

    private val log = logger<TyeCliClient>()

    fun version(tyePath: String): ProcessOutput {
        val cliBuilder = TyeVersionCliBuilder(tyePath)

        val commandLine = cliBuilder.build()

        log.debug("Call version command: ${commandLine.commandLineString}")

        return ExecUtil.execAndGetOutput(commandLine)
    }

    fun init(tyePath: String, options: InitOptions): ProcessOutput {
        val cliBuilder = TyeInitCliBuilder(tyePath, options.workDirectory)

        cliBuilder.setPath(options.path)
        if (options.overwriteExistingFile) cliBuilder.setForce()

        val commandLine = cliBuilder.build()

        log.debug("Call init command: ${commandLine.commandLineString}")

        return ExecUtil.execAndGetOutput(commandLine)
    }

    fun build(tyePath: String, options: BuildOptions): ProcessOutput {
        val cliBuilder = TyeBuildCliBuilder(tyePath, options.workDirectory)

        cliBuilder.setPath(options.path)

        val commandLine = cliBuilder.build()

        log.debug("Call build command: ${commandLine.commandLineString}")

        return ExecUtil.execAndGetOutput(commandLine)
    }

    fun push(tyePath: String, options: PushOptions): ProcessOutput {
        val cliBuilder = TyePushCliBuilder(tyePath, options.workDirectory)

        cliBuilder.setPath(options.path)

        val commandLine = cliBuilder.build()

        log.debug("Call push command: ${commandLine.commandLineString}")

        return ExecUtil.execAndGetOutput(commandLine)
    }

    fun run(tyePath: String, options: RunOptions): ColoredProcessHandler {
        val cliBuilder = TyeRunCliBuilder(tyePath, options.workDirectory)

        cliBuilder.setPath(options.path)
        if (options.port != DEFAULT_PORT) cliBuilder.setPort(options.port)
        if (options.noBuild) cliBuilder.setNoBuild()
        if (options.docker) cliBuilder.setDocker()
        if (options.dashboard) cliBuilder.setDashboard()
        if (options.watch) cliBuilder.setWatch()
        if (options.verbosity != DEFAULT_VERBOSITY) cliBuilder.setVerbosity(options.verbosity)
        if (!options.tags.isNullOrBlank()) cliBuilder.setTags(options.tags)
        if (!options.debug.isNullOrBlank()) cliBuilder.setDebug(options.debug)
        if (!options.framework.isNullOrBlank()) cliBuilder.setFramework(options.framework)
        if (options.logsProvider != DEFAULT_LOGS_PROVIDER) cliBuilder.setLogs(
            options.logsProvider,
            options.logsProviderUrl
        )
        if (options.tracesProvider != DEFAULT_TRACES_PROVIDER) cliBuilder.setTraces(
            options.tracesProvider,
            options.tracesProviderUrl
        )

        val commandLine = cliBuilder.build()

        log.debug("Call run command: ${commandLine.commandLineString}")

        return KillableColoredProcessHandler.Silent(commandLine)
    }
}