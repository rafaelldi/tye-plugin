package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentLogManager
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

class TyeServerRuntimeInstance(
    configuration: TyeHostConfiguration,
    private val taskExecutor: ServerTaskExecutor
) : ServerRuntimeInstance<TyeDeploymentConfiguration>() {

    private val tyeApplicationManager: TyeApplicationManager = TyeApplicationManager(configuration.hostAddress)

    override fun deploy(
        task: DeploymentTask<TyeDeploymentConfiguration>,
        logManager: DeploymentLogManager,
        callback: DeploymentOperationCallback
    ) {
        throw UnsupportedOperationException()
    }

    override fun computeDeployments(callback: ComputeDeploymentsCallback) {
        taskExecutor.submit({
            try {
                tyeApplicationManager.getServices().forEach {
                    val deployment = callback.addDeployment(it.applicationName, it, it.status, it.statusText)
                    it.setDeploymentModel(deployment)
                }

                callback.succeeded()
            } catch (e: ConnectException) {
                callback.errorOccurred("Cannot connect to the host")
            }
        }, callback)
    }

    fun connect(callback: ServerConnector.ConnectionCallback<TyeDeploymentConfiguration>) {
        taskExecutor.submit({
            try {
                tyeApplicationManager.connect()

                callback.connected(this)
            } catch (e: ConnectException) {
                callback.errorOccurred("Cannot connect to the host")
            }
        }, callback)
    }

    override fun disconnect() {
        taskExecutor.submit {
            tyeApplicationManager.disconnect()
        }
    }
}