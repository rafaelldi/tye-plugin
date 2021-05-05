package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.intellij.openapi.components.Service

@Service
final class TyeApiService {
    private val client: TyeApiClient

    init {
        client = TyeApiClient("http://localhost:8000")
    }

    suspend fun getServices(): List<com.github.rafaelldi.tyeplugin.api.model.Service>{
        return client.getServices()
    }
}