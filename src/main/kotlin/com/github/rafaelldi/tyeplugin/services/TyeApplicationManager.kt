package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.model.toModel
import com.github.rafaelldi.tyeplugin.remoteServer.TyeDeploymentConfiguration
import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.github.rafaelldi.tyeplugin.runtimes.TyeBaseRuntime
import com.intellij.execution.ExecutionException
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.components.service
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

class TyeApplicationManager(private val tyeHost: Url) {
    private var currentApplicationRuntime: TyeApplicationRuntime? = null

    fun runApplication(
        deploymentTask: DeploymentTask<TyeDeploymentConfiguration>
    ): TyeApplicationRuntime {
        val tyePathProvider = deploymentTask.project.service<TyePathProvider>()
        val tyePath = tyePathProvider.getPath() ?: throw ExecutionException("Tye path not specified.")

        val applicationRuntime = TyeApplicationRuntime(deploymentTask.executionEnvironment.runProfile.name, true, this)

        val cliClient = service<TyeCliClient>()
        val runOptions = createRunOptions(deploymentTask, tyeHost)
        val processHandler = cliClient.run(tyePath, runOptions)
        ProcessTerminatedListener.attach(processHandler)

        applicationRuntime.setUpProcessHandler(processHandler)
        currentApplicationRuntime = applicationRuntime

        return applicationRuntime
    }

    private fun createRunOptions(task: DeploymentTask<TyeDeploymentConfiguration>, host: Url): TyeCliClient.RunOptions =
        TyeCliClient.RunOptions(
            task.configuration.pathArgument!!,
            task.project.basePath,
            host.port,
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

    fun waitForReadiness() {
        runBlocking {
            val client = service<TyeApiClient>()
            for (i in 1..10) {
                try {
                    client.getApplication(tyeHost)
                } catch (e: ConnectException) {
                    delay(500)
                }
            }
        }
    }

    fun updateApplication() {
        if (currentApplicationRuntime == null) {
            currentApplicationRuntime = TyeApplicationRuntime("Tye Application", false, this)
        }

        runBlocking {
            val apiClient = service<TyeApiClient>()

            if (currentApplicationRuntime?.isModelNotInitialized() != false) {
                val applicationDto = apiClient.getApplication(tyeHost)
                val applicationModel = applicationDto.toModel()
                currentApplicationRuntime?.updateModel(applicationModel)
            }

            try {
                val services = apiClient.getServices(tyeHost)
                val serviceModels = services.mapNotNull { it.toModel() }
                currentApplicationRuntime?.updateServices(serviceModels)
            } catch (_: ConnectException) {
                //do nothing
            }
        }
    }

    fun getRuntimes(): List<TyeBaseRuntime> {
        val applicationRuntime = currentApplicationRuntime ?: return emptyList()

        val runtimes = mutableListOf<TyeBaseRuntime>()

        if (!applicationRuntime.isDeployable) {
            runtimes.add(applicationRuntime)
        }

        for (serviceRuntime in applicationRuntime.getServices()) {
            runtimes.add(serviceRuntime)
            runtimes.addAll(serviceRuntime.getReplicas())
        }

        return runtimes.toList()
    }

    fun shutdownApplication() {
        runBlocking {
            val apiClient = service<TyeApiClient>()
            try {
                apiClient.controlPlaneShutdown(tyeHost)
            } catch (_: ConnectException) {
                //do nothing
            }
            currentApplicationRuntime = null
        }
    }
}
