package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.remoteServer.deployment.TyeDeploymentConfiguration
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.remoteServer.configuration.deployment.DeploymentSource
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentLogManager
import com.intellij.remoteServer.runtime.deployment.DeploymentRuntime
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance
import kotlinx.coroutines.runBlocking

class TyeServerRuntimeInstance(
    private val configuration: TyeHostConfiguration,
    private val taskExecutor: ServerTaskExecutor
) : ServerRuntimeInstance<TyeDeploymentConfiguration>() {
    private var tyeApplicationManager: TyeApplicationManager = service()
    private val log = logger<TyeServerRuntimeInstance>()

    fun connect(callback: ServerConnector.ConnectionCallback<TyeDeploymentConfiguration>) {
        log.info("Server connected")
        callback.connected(this)
    }

    override fun deploy(
        task: DeploymentTask<TyeDeploymentConfiguration>,
        logManager: DeploymentLogManager,
        callback: DeploymentOperationCallback
    ) {
        taskExecutor.submit({
            runBlocking {
                tyeApplicationManager.runApplication(configuration.hostUrl, task, callback)
            }
        }, callback)
    }

    override fun computeDeployments(callback: ComputeDeploymentsCallback) {
        taskExecutor.submit({
            runBlocking {
                tyeApplicationManager.refreshApplication(configuration.hostUrl, callback)
            }
        }, callback)
    }

    override fun disconnect() {
        log.info("Server disconnected")
    }

    override fun getDeploymentName(source: DeploymentSource, configuration: TyeDeploymentConfiguration): String =
        "Tye Application"

    override fun getRuntimeDeploymentName(
        deploymentRuntime: DeploymentRuntime,
        source: DeploymentSource,
        configuration: TyeDeploymentConfiguration
    ): String = "Tye Application"
}