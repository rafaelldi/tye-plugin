package com.github.rafaelldi.tyeplugin.run

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
        val pathArgument = runConfig.pathArgument?.path ?: throw ExecutionException("Path argument not specified.")
        val workingDirectory = runConfig.pathArgument?.parent?.path

        val arguments = mutableListOf<String>()
        arguments.add("run")
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
