package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.services.TyeManager
import com.intellij.openapi.components.service
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentLogManager
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance
import kotlinx.coroutines.runBlocking
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
        throw UnsupportedOperationException()
    }

    override fun computeDeployments(callback: ComputeDeploymentsCallback) {
        taskExecutor.submit({
            //val applications = mutableListOf<CloudApplicationRuntime>()
            //applications.add(app1)
            //applications.add(app2)

            val app1 = ServiceApplicationRuntime("temp1")
            val dep1 = callback.addDeployment(app1.applicationName, app1, app1.status, app1.statusText)
            app1.setDeploymentModel(dep1)

            val app2 = ServiceApplicationRuntime("temp2")
            val dep2 = callback.addDeployment(app2.applicationName, app2, app2.status, app2.statusText)
            app2.setDeploymentModel(dep2)

            callback.succeeded()
        }, callback)
    }

    override fun disconnect() {
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