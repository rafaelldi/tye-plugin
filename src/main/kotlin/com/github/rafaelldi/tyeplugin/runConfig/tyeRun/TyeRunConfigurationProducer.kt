package com.github.rafaelldi.tyeplugin.runConfig.tyeRun

import com.github.rafaelldi.tyeplugin.TyeConstants.TYE_FILE_NAME
import com.github.rafaelldi.tyeplugin.runConfig.TyeConfigurationFactory
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

class TyeRunConfigurationProducer : LazyRunConfigurationProducer<TyeRunConfiguration>() {
    override fun getConfigurationFactory(): ConfigurationFactory =
        TyeConfigurationFactory(ConfigurationTypeUtil.findConfigurationType(TyeConfigurationType::class.java))

    override fun isConfigurationFromContext(
        configuration: TyeRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        val file = context.psiLocation?.containingFile ?: return false
        val currentFile = file.virtualFile
        return currentFile != null && currentFile.path == configuration.pathArgument?.path
    }

    override fun setupConfigurationFromContext(
        configuration: TyeRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        val file = context.psiLocation?.containingFile ?: return false

        if (file.name != TYE_FILE_NAME) {
            return false
        }

        configuration.pathArgument = file.virtualFile
        configuration.name = configuration.suggestedName()
        return true
    }
}
