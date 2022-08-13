package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.model.TyeApplication
import com.github.rafaelldi.tyeplugin.model.TyeService
import com.github.rafaelldi.tyeplugin.model.toModel
import com.github.rafaelldi.tyeplugin.remoteServer.deployment.TyeDeploymentConfiguration
import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import com.intellij.remoteServer.runtime.deployment.ServerRuntimeInstance
import io.ktor.http.*
import kotlinx.coroutines.delay
import java.net.ConnectException
import java.util.concurrent.ConcurrentHashMap

@Service
class TyeApplicationManager {
    companion object {
        private const val TYE_APPLICATION_NAME = "Tye Application"
    }

    private val applications: ConcurrentHashMap<String, TyeApplicationRuntime> = ConcurrentHashMap()

    suspend fun runApplication(
        host: Url,
        deploymentTask: DeploymentTask<TyeDeploymentConfiguration>,
        callback: ServerRuntimeInstance.DeploymentOperationCallback
    ) {
        if (applications.contains(host.toString())) {
            return
        }

        val applicationRuntime = TyeApplicationRuntime(TYE_APPLICATION_NAME, host, false)
        callback.started(applicationRuntime)

        applicationRuntime.run(deploymentTask)
        applications[host.toString()] = applicationRuntime

        val client = service<TyeApiClient>()
        for (i in 1..20) {
            val model = getApplicationModel(client, applicationRuntime.host)
            if (model != null) {
                applicationRuntime.updateModel(model)
                break
            }
            delay(500)
        }

        callback.succeeded(applicationRuntime)
    }

    suspend fun refreshApplication(host: Url, callback: ServerRuntimeInstance.ComputeDeploymentsCallback) {
        val client = service<TyeApiClient>()

        val applicationRuntime = applications[host.toString()]
        if (applicationRuntime != null) {
            if (!isApplicationLive(client, applicationRuntime)) {
                applications.remove(applicationRuntime.host.toString())
                applicationRuntime.clearRuntimes()
                applicationRuntime.removeDeployment()
                callback.succeeded()
                return
            }

            refreshApplicationServices(client, applicationRuntime, callback)
            callback.succeeded()
        } else {
            val newApplicationModel = getApplicationModel(client, host)
            if (newApplicationModel == null) {
                callback.succeeded()
                return
            }

            val newApplicationRuntime = TyeApplicationRuntime(TYE_APPLICATION_NAME, host, true)
            newApplicationRuntime.updateModel(newApplicationModel)
            applications[newApplicationRuntime.host.toString()] = newApplicationRuntime

            refreshApplicationServices(client, newApplicationRuntime, callback)
            callback.succeeded()
        }
    }

    private suspend fun refreshApplicationServices(
        client: TyeApiClient,
        applicationRuntime: TyeApplicationRuntime,
        callback: ServerRuntimeInstance.ComputeDeploymentsCallback
    ) {
        val serviceModels = getServiceModels(client, applicationRuntime.host)
        applicationRuntime.updateServices(serviceModels)
        applicationRuntime.getRuntimes().forEach {
            val deployment = callback.addDeployment(it.applicationName, it, it.status, it.statusText)
            it.setDeploymentModel(deployment)
        }
    }

    private suspend fun isApplicationLive(client: TyeApiClient, runtime: TyeApplicationRuntime) =
        try {
            client.getApplication(runtime.host)
            true
        } catch (e: ConnectException) {
            thisLogger().info("Cannot connect to the host")
            val handler = runtime.getProcessHandler()
            handler != null && !handler.isProcessTerminated
        }

    private suspend fun getApplicationModel(client: TyeApiClient, host: Url): TyeApplication? {
        return try {
            val applicationDto = client.getApplication(host)
            applicationDto.toModel()
        } catch (e: ConnectException) {
            thisLogger().info("Cannot connect to the host")
            null
        }
    }

    private suspend fun getServiceModels(client: TyeApiClient, host: Url): List<TyeService> {
        return try {
            val servicesDto = client.getServices(host)
            servicesDto.mapNotNull { it.toModel() }
        } catch (e: ConnectException) {
            thisLogger().info("Cannot connect to the host")
            emptyList()
        }
    }

    suspend fun shutdownApplication(host: Url) {
        val existingApplicationRuntime = applications[host.toString()] ?: return

        val apiClient = service<TyeApiClient>()
        try {
            apiClient.controlPlaneShutdown(existingApplicationRuntime.host)
        } catch (e: ConnectException) {
            thisLogger().warn("Cannot connect to the host")
        }
    }
}
