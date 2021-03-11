package com.github.rafaelldi.tyeplugin.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class TyeRunConfiguration(project: Project, factory: TyeConfigurationFactory, name: String) :
    RunConfigurationBase<TyeCommandLineState>(project, factory, name) {

    var tyeFile: VirtualFile? = null

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = TyeSettingsEditor(project)

    override fun checkConfiguration() {
        if (tyeFile == null) {
            throw RuntimeConfigurationError("No tye.yaml file specified.")
        }
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return TyeCommandLineState(environment, this)
    }
}