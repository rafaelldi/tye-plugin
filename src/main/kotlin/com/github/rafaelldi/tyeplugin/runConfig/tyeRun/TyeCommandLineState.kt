package com.github.rafaelldi.tyeplugin.runConfig.tyeRun

import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.services.TyeGlobalToolPathProvider
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project

open class TyeCommandLineState(
    environment: ExecutionEnvironment,
    private val runConfig: TyeRunConfiguration,
    private val project: Project
) : CommandLineState(environment) {
    private val tyeCliClient = service<TyeCliClient>()
    private val tyePathProvider: TyeGlobalToolPathProvider = project.service()

    override fun startProcess(): ProcessHandler {
        val tyePath = tyePathProvider.getPath() ?: throw ExecutionException("Tye path not specified.")

        val options = buildOptions()
        val commandLine = tyeCliClient.run(tyePath, options)

        val handler = KillableProcessHandler(commandLine)
        ProcessTerminatedListener.attach(handler, environment.project)

        return handler
    }

    private fun buildOptions(): TyeCliClient.RunOptions = TyeCliClient.RunOptions(
        runConfig.pathArgument?.path ?: throw ExecutionException("Path argument not specified."),
        project.basePath,
        runConfig.portArgument,
        runConfig.noBuildArgument,
        runConfig.dockerArgument,
        runConfig.dashboardArgument,
        runConfig.verbosityArgument,
        runConfig.tagsArgument,
        runConfig.logsProvider.argumentName,
        if (runConfig.logsProvider.isProviderUrlEnabled()) runConfig.logsProviderUrl else null,
        runConfig.tracesProvider.argumentName,
        if (runConfig.tracesProvider.isProviderUrlEnabled()) runConfig.tracesProviderUrl else null
    )
}
