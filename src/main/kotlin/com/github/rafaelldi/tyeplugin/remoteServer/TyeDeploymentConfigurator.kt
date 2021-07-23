package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.configuration.deployment.DeploymentConfigurator
import com.intellij.remoteServer.configuration.deployment.DeploymentSource

class TyeDeploymentConfigurator(private val project: Project) : DeploymentConfigurator<TyeDeploymentConfiguration, TyeHostConfiguration>() {
    override fun getAvailableDeploymentSources(): MutableList<DeploymentSource> {
        TODO("Not yet implemented")
    }

    override fun createDefaultConfiguration(source: DeploymentSource): TyeDeploymentConfiguration {
        TODO("Not yet implemented")
    }

    override fun createEditor(
        source: DeploymentSource,
        server: RemoteServer<TyeHostConfiguration>?
    ): SettingsEditor<TyeDeploymentConfiguration>? {
        TODO("Not yet implemented")
    }
}