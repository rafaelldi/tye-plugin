package com.github.rafaelldi.tyeplugin.run

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

        val arguments = mutableListOf<String>()
        arguments.add("run")

        if (runConfig.portArgument != DEFAULT_PORT) {
            arguments.add("--port")
            arguments.add(runConfig.portArgument.toString())
        }

        if (runConfig.noBuildArgument) {
            arguments.add("--no-build")
        }

        if (runConfig.dockerArgument) {
            arguments.add("--docker")
        }

        if (runConfig.dashboardArgument) {
            arguments.add("--dashboard")
        }

        if (runConfig.verbosityArgument != INFO_VERBOSITY) {
            arguments.add("--verbosity")
            arguments.add(runConfig.verbosityArgument)
        }

        if (!runConfig.tagsArgument.isNullOrBlank()) {
            arguments.add("--tags")
            arguments.add(runConfig.tagsArgument!!)
        }

        if (runConfig.logsProvider != LogsProvider.NONE) {
            arguments.add("--logs")

            var logsProviderArg = runConfig.logsProvider.argumentName
            if (runConfig.logsProvider.isProviderUrlEnabled() && !runConfig.logsProviderUrl.isNullOrBlank())
                logsProviderArg += "=${runConfig.logsProviderUrl}"

            arguments.add(logsProviderArg)
        }

        if (runConfig.tracesProvider != TracesProvider.NONE) {
            arguments.add("--dtrace")

            var tracesProviderArg = runConfig.tracesProvider.argumentName
            if (runConfig.tracesProvider.isProviderUrlEnabled() && !runConfig.tracesProviderUrl.isNullOrBlank())
                tracesProviderArg += "=${runConfig.tracesProviderUrl}"

            arguments.add(tracesProviderArg)
        }

        arguments.add(pathArgument)

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
}
