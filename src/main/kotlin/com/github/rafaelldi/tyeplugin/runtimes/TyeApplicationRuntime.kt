package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.remoteServer.deployment.TyeDeploymentConfiguration
import com.github.rafaelldi.tyeplugin.services.TyePathProvider
import com.intellij.execution.ExecutionException
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.net.ConnectException

class TyeApplicationRuntime(applicationName: String, val host: Url) : TyeBaseRuntime(applicationName) {
    private val serviceRuntimes: MutableMap<String, TyeServiceRuntime<TyeService>> = mutableMapOf()
    private var processHandler: ColoredProcessHandler? = null
    private var model: TyeApplication? = null

    fun run(deploymentTask: DeploymentTask<TyeDeploymentConfiguration>) {
        val tyePathProvider = deploymentTask.project.service<TyePathProvider>()
        val tyePath = tyePathProvider.getPath() ?: throw ExecutionException("Tye path not specified.")

        val cliClient = service<TyeCliClient>()
        val runOptions = createRunOptions(deploymentTask, host)
        val handler = cliClient.run(tyePath, runOptions)
        ProcessTerminatedListener.attach(handler)
        processHandler = handler
    }

    private fun createRunOptions(task: DeploymentTask<TyeDeploymentConfiguration>, host: Url): TyeCliClient.RunOptions =
        TyeCliClient.RunOptions(
            task.configuration.pathArgument!!,
            task.project.basePath,
            host.port,
            task.configuration.noBuildArgument,
            task.configuration.dockerArgument,
            task.configuration.dashboardArgument,
            task.configuration.watchArgument,
            task.configuration.verbosityArgument.value,
            task.configuration.tagsArgument,
            task.configuration.debugArgument,
            task.configuration.logsProvider.argumentName,
            task.configuration.logsProviderUrl,
            task.configuration.tracesProvider.argumentName,
            task.configuration.tracesProviderUrl
        )

    fun isLive(): Boolean = runBlocking {
        val client = service<TyeApiClient>()
        try {
            client.getApplication(host)
            return@runBlocking true
        } catch (e: ConnectException) {
            return@runBlocking false
        }
    }

    fun waitForReadiness() = runBlocking {
        for (i in 1..10) {
            if (isLive()) {
                break
            } else {
                delay(500)
            }
        }
    }

    fun refresh(): List<TyeBaseRuntime> {
        if (model == null) {
            val newModel = getModel()
            model = newModel
        }

        val updatedServices = getServices()
        refreshServiceRuntimes(updatedServices)

        val runtimes = mutableListOf<TyeBaseRuntime>()
        runtimes.add(this)
        for (serviceRuntime in serviceRuntimes.values.toList()) {
            runtimes.add(serviceRuntime)
            runtimes.addAll(serviceRuntime.getReplicas())
        }

        return runtimes.toList()
    }

    private fun getModel(): TyeApplication? = runBlocking {
        val client = service<TyeApiClient>()
        try {
            val applicationDto = client.getApplication(host)
            return@runBlocking applicationDto.toModel()
        } catch (e: ConnectException) {
            thisLogger().warn("Cannot connect to the host", e)
            return@runBlocking null
        }
    }

    private fun getServices(): List<TyeService> = runBlocking {
        val client = service<TyeApiClient>()
        try {
            val servicesDto = client.getServices(host)
            return@runBlocking servicesDto.mapNotNull { it.toModel() }
        } catch (e: ConnectException) {
            thisLogger().warn("Cannot connect to the host", e)
            return@runBlocking emptyList<TyeService>()
        }
    }

    private fun refreshServiceRuntimes(newServices: List<TyeService>) {
        val currentRuntimeNames = serviceRuntimes.keys.toHashSet()
        val newServiceNames = newServices.map { it.getServiceName() }.toHashSet()

        for (newService in newServices) {
            val newServiceName = newService.getServiceName()
            if (currentRuntimeNames.contains(newServiceName)) {
                serviceRuntimes[newServiceName]?.updateReplicas(newService)
            } else {
                val newRuntime = createServiceRuntime(newService, this)
                serviceRuntimes[newServiceName] = newRuntime
            }
        }

        for (deletedRuntimeName in currentRuntimeNames.subtract(newServiceNames)) {
            serviceRuntimes.remove(deletedRuntimeName)
        }
    }

    private fun createServiceRuntime(model: TyeService, parent: TyeApplicationRuntime): TyeServiceRuntime<TyeService> =
        when (model) {
            is TyeContainerService -> TyeServiceDockerRuntime(model, parent) as TyeServiceRuntime<TyeService>
            is TyeProjectService -> TyeServiceProjectRuntime(model, parent) as TyeServiceRuntime<TyeService>
            is TyeExecutableService -> TyeServiceExecutableRuntime(model, parent) as TyeServiceRuntime<TyeService>
            is TyeIngressService -> TyeServiceIngressRuntime(model, parent) as TyeServiceRuntime<TyeService>
        }

    override fun getSourceFile(): VirtualFile? {
        val path = model?.source ?: return null
        return LocalFileSystem.getInstance().findFileByPath(path)
    }

    fun getProcessHandler(): ColoredProcessHandler? = processHandler

    fun shutdown() {
        runBlocking {
            val apiClient = service<TyeApiClient>()
            try {
                apiClient.controlPlaneShutdown(host)
            } catch (e: ConnectException) {
                thisLogger().warn("Cannot connect to the host", e)
            }
        }
    }
}