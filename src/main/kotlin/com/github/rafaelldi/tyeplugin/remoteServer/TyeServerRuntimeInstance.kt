package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.services.TyeManager
import com.intellij.openapi.components.service
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentLogManager
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance
import com.intellij.remoteServer.util.CallbackWrapper
import com.intellij.remoteServer.util.CloudApplicationRuntime
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.Nls
import java.net.ConnectException

class TyeServerRuntimeInstance(
    private val configuration: TyeHostConfiguration,
    private val taskExecutor: ServerTaskExecutor
) : ServerRuntimeInstance<TyeDeploymentConfiguration>() {

    private var tyeManager: TyeManager = service()

    override fun deploy(
        task: DeploymentTask<TyeDeploymentConfiguration>,
        logManager: DeploymentLogManager,
        callback: DeploymentOperationCallback
    ) {

    }

    override fun computeDeployments(callback: ComputeDeploymentsCallback) {

    }

    override fun disconnect() {
        TODO("Not yet implemented")
    }

    fun connect(callback: ServerConnector.ConnectionCallback<TyeDeploymentConfiguration>) {
        taskExecutor.submit({
            try {
                runBlocking {
                    tyeManager.connect(configuration.hostAddress)
                }
                callback.connected(this)
            } catch (e: ConnectException) {
                callback.errorOccurred("Cannot connect to the host")
            }
        }, callback)
    }
}