package com.github.rafaelldi.tyeplugin

import com.intellij.execution.configurations.CommandLineState
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment

open class TyeCommandLineState(environment: ExecutionEnvironment, private val runConfig: TyeRunConfiguration) :
    CommandLineState(environment) {

    override fun startProcess(): ProcessHandler {
        val commandLine = GeneralCommandLine()
        val handler = KillableProcessHandler(commandLine)
        ProcessTerminatedListener.attach(handler, environment.project)
        return handler
    }
}
