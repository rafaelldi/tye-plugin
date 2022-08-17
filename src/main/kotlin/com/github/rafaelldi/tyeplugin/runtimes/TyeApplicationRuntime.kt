package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.cli.TyeCliClient
import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.remoteServer.deployment.TyeDeploymentConfiguration
import com.github.rafaelldi.tyeplugin.services.TyePathProvider
import com.intellij.execution.ExecutionException
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.openapi.components.service
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import io.ktor.http.*

class TyeApplicationRuntime(applicationName: String, val host: Url, val withMetrics: Boolean, val isExternal: Boolean) :
    TyeBaseRuntime(applicationName) {
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
            task.configuration.frameworkArgument,
            task.configuration.logsProvider.argumentName,
            task.configuration.logsProviderUrl,
            task.configuration.tracesProvider.argumentName,
            task.configuration.tracesProviderUrl
        )

    fun updateModel(application: TyeApplication) {
        model = application
    }

    fun updateServices(services: List<TyeService>) {
        val currentServiceNames = serviceRuntimes.keys.toSet()
        val serviceNames = services.map { it.getName() }.toSet()

        for (service in services) {
            val serviceName = service.getName()
            val serviceRuntime = serviceRuntimes[serviceName]
            if (serviceRuntime != null) {
                serviceRuntime.update(service)
            } else {
                val newRuntime = createServiceRuntime(service, this)
                serviceRuntimes[serviceName] = newRuntime
            }
        }

        for (deletedServiceName in currentServiceNames.subtract(serviceNames)) {
            serviceRuntimes.remove(deletedServiceName)
        }
    }

    fun updateMetrics(metrics: List<TyeServiceMetrics>) {
        for (serviceMetrics in metrics) {
            val serviceRuntime = serviceRuntimes[serviceMetrics.service] ?: continue
            serviceRuntime.updateMetrics(serviceMetrics.metrics)
        }
    }

    fun getRuntimes(): List<TyeBaseRuntime> {
        val runtimes = mutableListOf<TyeBaseRuntime>()
        runtimes.add(this)
        for (serviceRuntime in serviceRuntimes.values.toList()) {
            runtimes.add(serviceRuntime)
            runtimes.addAll(serviceRuntime.getRuntimes())
        }

        return runtimes.toList()
    }

    fun clearRuntimes() {
        serviceRuntimes.forEach {
            it.value.clearRuntimes()
            it.value.removeDeployment()
        }
        serviceRuntimes.clear()
    }

    @Suppress("UNCHECKED_CAST")
    private fun createServiceRuntime(
        model: TyeService,
        parent: TyeApplicationRuntime
    ): TyeServiceRuntime<TyeService> =
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
}