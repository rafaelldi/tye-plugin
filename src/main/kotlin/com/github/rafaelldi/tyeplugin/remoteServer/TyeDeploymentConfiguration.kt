package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.runConfig.tyeRun.LogsProvider
import com.github.rafaelldi.tyeplugin.runConfig.tyeRun.TracesProvider
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
    var verbosityArgument = Verbosity.INFO
    var tagsArgument: String? = null
    var logsProvider = LogsProvider.NONE
    var logsProviderUrl: String? = null
    var tracesProvider = TracesProvider.NONE
    var tracesProviderUrl: String? = null

    override fun checkConfiguration(server: RemoteServer<*>?, deploymentSource: DeploymentSource?, project: Project?) {
        if (pathArgument.isNullOrEmpty()) {
            throw RuntimeConfigurationError("Path argument not specified.")
        }
    }
}

enum class Verbosity {
    DEBUG {
        override fun value(): String = "debug"
    },
    INFO {
        override fun value(): String = "info"
    },
    QUIET {
        override fun value(): String = "quiet"
    };

    open fun value(): String = error("Not implemented for $this")
}