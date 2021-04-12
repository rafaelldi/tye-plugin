package com.github.rafaelldi.tyeplugin.run

import com.github.rafaelldi.tyeplugin.cli.TyeRunCliBuilder
import com.github.rafaelldi.tyeplugin.run.OptionsConstants.DEFAULT_PORT
import com.github.rafaelldi.tyeplugin.run.OptionsConstants.INFO_VERBOSITY
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project

open class TyeCommandLineState(
    environment: ExecutionEnvironment,
    private val runConfig: TyeRunConfiguration,
    private val project: Project
) : CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val tyeToolPath = TyeSettingsState.getInstance(project).tyeToolPath
            ?: throw ExecutionException("Tye tool not found.")
        val pathArgument = runConfig.pathArgument?.path
            ?: throw ExecutionException("Path argument not specified.")
        val workingDirectory = runConfig.pathArgument?.parent?.path

        val arguments = buildArguments(pathArgument)

        return createHandler(workingDirectory, tyeToolPath, arguments)
    }

    private fun createHandler(
        workingDirectory: String?,
        tyeToolPath: String,
        arguments: List<String>
    ): OSProcessHandler {
        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withWorkDirectory(workingDirectory)
            .withExePath(tyeToolPath)
            .withParameters(arguments)

        val handler = OSProcessHandler(commandLine)
        handler.startNotify()
        ProcessTerminatedListener.attach(handler, environment.project)
        return handler
    }

    private fun buildArguments(pathArgument: String): List<String> {
        val cliBuilder = TyeRunCliBuilder(pathArgument)

        if (runConfig.portArgument != DEFAULT_PORT) {
            cliBuilder.setPort(runConfig.portArgument)
        }

        if (runConfig.noBuildArgument) {
            cliBuilder.setNoBuild()
        }

        if (runConfig.dockerArgument) {
            cliBuilder.setDocker()
        }

        if (runConfig.dashboardArgument) {
            cliBuilder.setDashboard()
        }

        if (runConfig.verbosityArgument != INFO_VERBOSITY) {
            cliBuilder.setVerbosity(runConfig.verbosityArgument)
        }

        if (!runConfig.tagsArgument.isNullOrBlank()) {
            cliBuilder.setTags(runConfig.tagsArgument!!)
        }

        if (runConfig.logsProvider != LogsProvider.NONE) {
            val logsProviderUrl = if (runConfig.logsProvider.isProviderUrlEnabled()) runConfig.logsProviderUrl else null
            cliBuilder.setLogs(runConfig.logsProvider.argumentName, logsProviderUrl)
        }

        if (runConfig.tracesProvider != TracesProvider.NONE) {
            val tracesProviderUrl =
                if (runConfig.tracesProvider.isProviderUrlEnabled()) runConfig.tracesProviderUrl else null
            cliBuilder.setTraces(runConfig.tracesProvider.argumentName, tracesProviderUrl)
        }

        return cliBuilder.build()
    }
}
