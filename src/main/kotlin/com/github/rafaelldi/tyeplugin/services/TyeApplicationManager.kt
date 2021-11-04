package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.model.TyeApplication
import com.github.rafaelldi.tyeplugin.model.toService
import com.github.rafaelldi.tyeplugin.runtimes.TyeBaseRuntime
import com.github.rafaelldi.tyeplugin.runtimes.toRuntime
import com.intellij.openapi.components.service
import kotlinx.coroutines.runBlocking

class TyeApplicationManager(private val host: String) {
    private val client: TyeApiClient = service()
    private var application: TyeApplication? = null

    fun connect() {
        runBlocking {
            val dto = client.getApplication(host)
            application = TyeApplication(dto.id, dto.name, dto.source)
        }
    }

    fun getRuntimes(): List<TyeBaseRuntime> {
        val currentApplication = application ?: return emptyList()

        val runtimes = mutableListOf<TyeBaseRuntime>()

        val applicationRuntime = currentApplication.toRuntime(this)
        runtimes.add(applicationRuntime)

        if (currentApplication.isServicesEmpty()) {
            updateApplication()
        }

        currentApplication.getServices().forEach { service ->
            val serviceRuntime = service.toRuntime(applicationRuntime)
            runtimes.add(serviceRuntime)

            val replicaRuntimes = service.replicas?.map { it.toRuntime(serviceRuntime) }
            if (!replicaRuntimes.isNullOrEmpty()) {
                runtimes.addAll(replicaRuntimes)
            }
        }

        return runtimes
    }

    fun shutdownApplication() {
        runBlocking {
            client.controlPlaneShutdown(host)
        }
        application = null
    }

    fun disconnect() {
        application = null
    }

    private fun updateApplication() {
        runBlocking {
            val servicesDto = client.getServices(host)
            application?.updateServices(servicesDto.mapNotNull { it.toService() })
        }
    }
}
