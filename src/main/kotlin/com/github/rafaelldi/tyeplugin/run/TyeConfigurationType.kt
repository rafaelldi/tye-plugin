package com.github.rafaelldi.tyeplugin.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import icons.TyeIcons

class TyeConfigurationType : ConfigurationType {
    override fun getDisplayName() = "Tye"

    override fun getConfigurationTypeDescription() = "Tye run command"

    override fun getIcon() = TyeIcons.TYE

    override fun getId() = "TYE_RUN_CONFIGURATION"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(TyeConfigurationFactory(this))
}
