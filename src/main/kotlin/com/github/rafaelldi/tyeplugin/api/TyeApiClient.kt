package com.github.rafaelldi.tyeplugin.api

import com.github.rafaelldi.tyeplugin.api.model.ServiceDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.get

class TyeApiClient(private val host: String) {
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getServices(): List<ServiceDto> {
        return httpClient.get("$host/api/v1/services")
    }
}
