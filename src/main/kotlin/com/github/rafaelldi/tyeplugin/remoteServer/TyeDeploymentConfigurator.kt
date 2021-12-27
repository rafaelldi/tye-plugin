package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.configuration.deployment.DeploymentConfigurator
import com.intellij.remoteServer.configuration.deployment.DeploymentSource

class TyeDeploymentConfigurator(private val project: Project) :
    DeploymentConfigurator<TyeDeploymentConfiguration, TyeHostConfiguration>() {
    override fun getAvailableDeploymentSources(): MutableList<DeploymentSource> =
        emptyList<DeploymentSource>().toMutableList()

    override fun createDefaultConfiguration(source: DeploymentSource): TyeDeploymentConfiguration =
        TyeDeploymentConfiguration()

    override fun createEditor(
        source: DeploymentSource,
        server: RemoteServer<TyeHostConfiguration>?
    ): SettingsEditor<TyeDeploymentConfiguration> = TyeDeploymentEditor(project)

    override fun suggestConfigurationName(
        deploymentSource: DeploymentSource,
        deploymentConfiguration: TyeDeploymentConfiguration
    ): String = "Tye Run"
}