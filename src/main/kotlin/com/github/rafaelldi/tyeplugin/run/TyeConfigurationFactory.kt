package com.github.rafaelldi.tyeplugin.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project

class TyeConfigurationFactory(type: TyeRunConfigurationType) : ConfigurationFactory(type) {
    companion object {
        private const val FACTORY_NAME = "Tye configuration factory"
    }

    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return TyeRunConfiguration(project, this, "Tye")
    }

    override fun getName() = FACTORY_NAME

    override fun getId() = FACTORY_NAME
}
