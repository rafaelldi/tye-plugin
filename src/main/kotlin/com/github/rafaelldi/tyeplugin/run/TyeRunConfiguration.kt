package com.github.rafaelldi.tyeplugin.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class TyeRunConfiguration(project: Project, factory: TyeConfigurationFactory, name: String) :
    RunConfigurationBase<TyeCommandLineState>(project, factory, name) {
    companion object {
        const val DEFAULT_PORT_ARGUMENT: Int = 8000
    }

    var pathArgument: VirtualFile? = project.basePath?.let { LocalFileSystem.getInstance().findFileByPath(it) }
    var noBuildArgument = false
    var portArgument: Int = DEFAULT_PORT_ARGUMENT
    var dockerArgument = false
    var dashboardArgument = false

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = TyeSettingsEditor()

    override fun checkConfiguration() {
        if (pathArgument == null) {
            throw RuntimeConfigurationError("Path argument not specified.")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return TyeCommandLineState(environment, this, project)
    }
}
