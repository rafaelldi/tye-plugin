package com.github.rafaelldi.tyeplugin.runConfig.tyeRun

import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.process.OSProcessHandler
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

    override fun startProcess(): ProcessHandler {
        val tyeCliClient = project.service<TyeCliClient>()
        val options = buildOptions()
        val commandLine = tyeCliClient.run(options)

        val handler = OSProcessHandler(commandLine)
        handler.startNotify()
        ProcessTerminatedListener.attach(handler, environment.project)

        return handler
    }

    private fun buildOptions(): TyeCliClient.RunOptions = TyeCliClient.RunOptions(
        runConfig.pathArgument?.path ?: throw ExecutionException("Path argument not specified."),
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
