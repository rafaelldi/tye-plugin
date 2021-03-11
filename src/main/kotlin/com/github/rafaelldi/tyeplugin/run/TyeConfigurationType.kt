package com.github.rafaelldi.tyeplugin.run

import com.github.rafaelldi.tyeplugin.TyeIcons
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType

class TyeConfigurationType : ConfigurationType {
    override fun getDisplayName() = "Tye"

    override fun getConfigurationTypeDescription() = "Run tye script"

    override fun getIcon() = TyeIcons.TYE

    override fun getId() = "TYE_RUN_CONFIGURATION"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(TyeConfigurationFactory(this))
}
