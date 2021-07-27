package com.github.rafaelldi.tyeplugin.cli

import com.github.rafaelldi.tyeplugin.services.TyeToolPathProvider
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project

@Service
class TyeCliClient(private val project: Project) {
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

    private val tyeToolPathProvider = project.service<TyeToolPathProvider>()
    private val log = Logger.getInstance(TyeCliClient::class.java)

    fun init(path: String, options: InitOptions): GeneralCommandLine {
        val tyePath = tyeToolPathProvider.getPath() ?: throw ExecutionException("Tye tool not found.")

        val cliBuilder = TyeInitCliBuilder(path)

        if (options.overwriteExistingFile) cliBuilder.setForce()

        val arguments = cliBuilder.build()

        log.info("Call init command with arguments $arguments")

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(project.basePath)
            .withExePath(tyePath)
            .withParameters(arguments)
    }

    fun run(path: String, options: RunOptions): GeneralCommandLine {
        val tyePath = tyeToolPathProvider.getPath() ?: throw ExecutionException("Tye tool not found.")

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

        log.info("Call run command with arguments $arguments")

        return GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(project.basePath)
            .withExePath(tyePath)
            .withParameters(arguments)
    }
}