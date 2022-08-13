package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.remoteServer.TyeHostType
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.remoteServer.ServerType
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentRuntime
import com.intellij.remoteServer.util.AgentTaskExecutor
import com.intellij.remoteServer.util.CloudApplicationRuntime

sealed class TyeBaseRuntime(applicationName: String?) : CloudApplicationRuntime(applicationName) {
    protected var parent: TyeBaseRuntime? = null

    override fun getCloudType(): ServerType<*> = TyeHostType.getInstance()

    override fun isUndeploySupported(): Boolean = false

    override fun undeploy(callback: UndeploymentTaskCallback) {
        throw UnsupportedOperationException()
    }

    fun removeDeployment() {
        deploymentModel.connection.undeploy(deploymentModel, null)
    }

    override fun getTaskExecutor(): ServerTaskExecutor {
        throw UnsupportedOperationException()
    }

    override fun getAgentTaskExecutor(): AgentTaskExecutor {
        throw UnsupportedOperationException()
    }

    override fun getParent(): DeploymentRuntime? = parent

    open fun getSourceFile(): VirtualFile? = null
}