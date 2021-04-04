package com.github.rafaelldi.tyeplugin.run

import com.github.rafaelldi.tyeplugin.run.OptionsConstants.DEFAULT_PORT
import com.github.rafaelldi.tyeplugin.run.OptionsConstants.INFO_VERBOSITY
import com.intellij.execution.Executor
import com.intellij.execution.configurations.LocatableConfigurationBase
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class TyeRunConfiguration(project: Project, factory: TyeConfigurationFactory, name: String) :
    LocatableConfigurationBase<TyeCommandLineState>(project, factory, name) {

    var pathArgument: VirtualFile? = project.basePath?.let { LocalFileSystem.getInstance().findFileByPath(it) }
    var noBuildArgument = false
    var portArgument: Int = DEFAULT_PORT
    var dockerArgument = false
    var dashboardArgument = false
    var verbosityArgument = INFO_VERBOSITY

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = TyeSettingsEditor()

    override fun checkConfiguration() {
        if (pathArgument == null) {
            throw RuntimeConfigurationError("Path argument not specified.")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState =
        TyeCommandLineState(environment, this, project)

    override fun suggestedName() = pathArgument?.nameWithoutExtension ?: "Tye"
}
