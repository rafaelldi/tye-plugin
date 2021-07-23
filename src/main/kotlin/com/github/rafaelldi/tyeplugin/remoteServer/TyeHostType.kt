package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.RemoteServerConfigurable
import com.intellij.remoteServer.ServerType
import com.intellij.remoteServer.configuration.deployment.DeploymentConfigurator
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import javax.swing.Icon

class TyeHostType : ServerType<TyeHostConfiguration>("tye") {
    companion object {
        fun getInstance(): TyeHostType {
            return EP_NAME.findExtension(TyeHostType::class.java)!!
        }
    }

    override fun getPresentableName(): String = "Tye"

    override fun getIcon(): Icon = AllIcons.General.Balloon

    override fun createDefaultConfiguration(): TyeHostConfiguration = TyeHostConfiguration.createDefault()

    override fun createServerConfigurable(configuration: TyeHostConfiguration): RemoteServerConfigurable =
        TyeHostConfigurable(configuration)

    override fun createDeploymentConfigurator(project: Project): DeploymentConfigurator<*, TyeHostConfiguration> =
        TyeDeploymentConfigurator(project)

    override fun createConnector(
        configuration: TyeHostConfiguration,
        asyncTasksExecutor: ServerTaskExecutor
    ): ServerConnector<*> = TyeConnector(configuration, asyncTasksExecutor)

    override fun getCustomToolWindowId(): String = "Services"
}