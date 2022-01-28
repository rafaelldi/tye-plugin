package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.intellij.remoteServer.configuration.deployment.DeploymentSource
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentLogManager
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance

class TyeServerRuntimeInstance(
    private val configuration: TyeHostConfiguration,
    private val taskExecutor: ServerTaskExecutor
) : ServerRuntimeInstance<TyeDeploymentConfiguration>() {
    private var tyeApplicationManager: TyeApplicationManager? = null

    fun connect(callback: ServerConnector.ConnectionCallback<TyeDeploymentConfiguration>) {
        taskExecutor.submit({
            tyeApplicationManager = TyeApplicationManager(configuration.hostUrl)
            callback.connected(this)
        }, callback)
    }

    override fun deploy(
        task: DeploymentTask<TyeDeploymentConfiguration>,
        logManager: DeploymentLogManager,
        callback: DeploymentOperationCallback
    ) {
        taskExecutor.submit({
            val runtime = tyeApplicationManager!!.runApplication(task)
            callback.started(runtime)

            tyeApplicationManager!!.waitForReadiness()
            callback.succeeded(runtime)
        }, callback)
    }

    override fun computeDeployments(callback: ComputeDeploymentsCallback) {
        taskExecutor.submit({
            tyeApplicationManager!!.updateApplication()

            val runtimes = tyeApplicationManager!!.getRuntimes()
            runtimes.forEach {
                val deployment = callback.addDeployment(it.applicationName, it, it.status, it.statusText)
                it.setDeploymentModel(deployment)
            }
            callback.succeeded()
        }, callback)
    }

    override fun disconnect() {
        tyeApplicationManager = null
    }

    override fun getDeploymentName(source: DeploymentSource, configuration: TyeDeploymentConfiguration): String =
        "Tye Application"
}