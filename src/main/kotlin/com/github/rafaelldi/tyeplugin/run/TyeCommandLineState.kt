package com.github.rafaelldi.tyeplugin.run

import com.github.rafaelldi.tyeplugin.settings.TyeSettings
import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment

open class TyeCommandLineState(environment: ExecutionEnvironment, private val runConfig: TyeRunConfiguration) :
    CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val tyeTool = TyeSettings.getInstance().tyeTool

        val tyeFilePath = runConfig.tyeFile?.path
        val workingDirectory = runConfig.tyeFile?.parent?.path

        val arguments = mutableListOf<String>()
        arguments.add("run")
        arguments.add(tyeFilePath.toString())

        val commandLine = GeneralCommandLine()
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withExePath(tyeTool)
            .withParameters(arguments)

        val handler = OSProcessHandler(commandLine)
        handler.startNotify()
        ProcessTerminatedListener.attach(handler, environment.project)
        return handler
    }
}
