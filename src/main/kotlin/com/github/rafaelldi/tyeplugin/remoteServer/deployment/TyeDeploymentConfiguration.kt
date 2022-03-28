package com.github.rafaelldi.tyeplugin.remoteServer.deployment

import com.github.rafaelldi.tyeplugin.remoteServer.*
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.configuration.deployment.DeploymentConfigurationBase
import com.intellij.remoteServer.configuration.deployment.DeploymentSource

class TyeDeploymentConfiguration : DeploymentConfigurationBase<TyeDeploymentConfiguration>() {
    var pathArgument: String? = null
    var noBuildArgument = false
    var dockerArgument = false
    var dashboardArgument = false
    var watchArgument = false
    var verbosityArgument = Verbosity.INFO
    var tagsArgument: String? = null
    var debugArgument: String? = null
    var logsProvider = LogsProvider.NONE
    var logsProviderUrl: String? = null
    var tracesProvider = TracesProvider.NONE
    var tracesProviderUrl: String? = null

    override fun checkConfiguration(
        server: RemoteServer<*>?,
        deploymentSource: DeploymentSource?,
        project: Project?
    ) {
        if (pathArgument.isNullOrEmpty()) {
            throw RuntimeConfigurationError("Path argument not specified.")
        }

        val configuration = server?.configuration as? TyeHostConfiguration ?: return
        if (configuration.hostUrl.host != "localhost") {
            throw RuntimeConfigurationError("Only localhost is supported for the server.")
        }
    }
}