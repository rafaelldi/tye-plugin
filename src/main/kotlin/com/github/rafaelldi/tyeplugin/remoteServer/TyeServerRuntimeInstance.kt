package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.remoteServer.deployment.TyeDeploymentConfiguration
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
        val manager = tyeApplicationManager ?: return

        val currentApplication = manager.getCurrentApplication()
        if (currentApplication != null) {
            callback.succeeded(currentApplication)
        } else {
            taskExecutor.submit({
                val runtime = manager.runApplication(task)
                callback.started(runtime)

                manager.waitForReadiness()
                callback.succeeded(runtime)
            }, callback)
        }
    }

    override fun computeDeployments(callback: ComputeDeploymentsCallback) {
        val manager = tyeApplicationManager ?: return

        taskExecutor.submit({
            manager.updateApplication()

            val runtimes = manager.getRuntimes()
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