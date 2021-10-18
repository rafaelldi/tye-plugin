package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.model.TyeApplication
import com.github.rafaelldi.tyeplugin.model.toService
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

    fun getApplication(): TyeApplication? {
        if (application?.isServicesEmpty() == true) {
            updateApplication()
        }
        return application
    }

    fun disconnect() {
        application = null
    }

    private fun updateApplication() {
        runBlocking {
            val servicesDto = client.getServices(host)
            application?.updateServices(servicesDto.map { it.toService() })
        }
    }
}
