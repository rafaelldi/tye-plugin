package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.model.toService
import com.intellij.openapi.components.Service

@Service
class TyeManager {
    private val client: TyeApiClient = TyeApiClient()

    suspend fun connect(host: String){
        val servicesDto = client.getServices(host)
        val services = servicesDto.map { it.toService() }
    }
}