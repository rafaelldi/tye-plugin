package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.runtimes.TyeDeploymentRuntime
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.intellij.execution.process.KillableColoredProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.components.service
import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentLogManager
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance
import java.net.ConnectException

class TyeServerRuntimeInstance(
    configuration: TyeHostConfiguration,
    private val taskExecutor: ServerTaskExecutor
) : ServerRuntimeInstance<TyeDeploymentConfiguration>() {

    private val tyeApplicationManager: TyeApplicationManager = TyeApplicationManager(configuration.hostAddress)
    private val tyeCliClient: TyeCliClient = service()

    override fun computeDeployments(callback: ComputeDeploymentsCallback) {
        taskExecutor.submit({
            try {
                val runtimes = tyeApplicationManager.getRuntimes()
                runtimes.forEach {
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

    override fun deploy(
        task: DeploymentTask<TyeDeploymentConfiguration>,
        logManager: DeploymentLogManager,
        callback: DeploymentOperationCallback
    ) {
        taskExecutor.submit({
            val tyePath = "tye"
            val options = TyeCliClient.RunOptions(
                task.configuration.pathArgument!!,
                task.project.basePath,
                8000,
                task.configuration.noBuildArgument,
                task.configuration.dockerArgument,
                task.configuration.dashboardArgument,
                task.configuration.verbosityArgument.value,
                task.configuration.tagsArgument,
                task.configuration.logsProvider.argumentName,
                task.configuration.logsProviderUrl,
                task.configuration.tracesProvider.argumentName,
                task.configuration.tracesProviderUrl
            )

            val commandLine = tyeCliClient.run(tyePath, options)
            val handler = KillableColoredProcessHandler(commandLine)
            ProcessTerminatedListener.attach(handler)
            handler.startNotify()

            val runtime = TyeDeploymentRuntime(handler)
            callback.started(runtime)
            callback.succeeded(runtime)
        }, callback)
    }

    override fun disconnect() {
        taskExecutor.submit {
            tyeApplicationManager.disconnect()
        }
    }
}