package com.github.rafaelldi.tyeplugin.runConfig.tyeRun

import com.github.rafaelldi.tyeplugin.runConfig.TyeConfigurationFactory
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import icons.TyeIcons

class TyeConfigurationType : ConfigurationType {
    override fun getDisplayName() = "Tye"

    override fun getConfigurationTypeDescription() = "Tye run command"

    override fun getIcon() = TyeIcons.TYE_RUN

    override fun getId() = "TYE_RUN_CONFIGURATION"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(TyeConfigurationFactory(this))
}
