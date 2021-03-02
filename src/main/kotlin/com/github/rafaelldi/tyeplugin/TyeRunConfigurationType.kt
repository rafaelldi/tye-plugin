package com.github.rafaelldi.tyeplugin

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.icons.AllIcons

class TyeRunConfigurationType : ConfigurationType {
    override fun getDisplayName() = "Tye"

    override fun getConfigurationTypeDescription() = "Run tye script"

    override fun getIcon() = AllIcons.General.Information

    override fun getId() = "TYE_RUN_CONFIGURATION"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(TyeConfigurationFactory(this))
}