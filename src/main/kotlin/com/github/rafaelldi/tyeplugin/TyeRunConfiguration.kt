package com.github.rafaelldi.tyeplugin

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project

class TyeRunConfiguration(project: Project, factory: TyeConfigurationFactory, name: String) :
    RunConfigurationBase<TyeCommandLineState>(project, factory, name) {

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = TyeSettingsEditor()

    override fun checkConfiguration() {
    }

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState {
        return TyeCommandLineState(environment, this)
    }
}
