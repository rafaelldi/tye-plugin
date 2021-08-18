package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.icons.AllIcons
import com.intellij.remoteServer.ServerType
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentStatus
import com.intellij.remoteServer.util.AgentTaskExecutor
import com.intellij.remoteServer.util.CloudApplicationRuntime

class ServiceApplicationRuntime(applicationName: String?) : CloudApplicationRuntime(applicationName) {

    override fun undeploy(callback: UndeploymentTaskCallback) {
        throw UnsupportedOperationException()
    }

    override fun getTaskExecutor(): ServerTaskExecutor {
        throw UnsupportedOperationException()
    }

    override fun getAgentTaskExecutor(): AgentTaskExecutor {
        throw UnsupportedOperationException()
    }

    override fun getCloudType(): ServerType<*> = TyeHostType.getInstance()

    override fun getStatus(): DeploymentStatus = DeploymentStatus(AllIcons.General.Layout, "Text", false)
}