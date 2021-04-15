package com.github.rafaelldi.tyeplugin.api

import com.github.rafaelldi.tyeplugin.api.model.Service
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get

class TyeApiClient {
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(JsonFeature)
    }

    suspend fun getServices(): List<Service> {
        return httpClient.get("http://localhost:8000/api/v1/services")
    }
}
