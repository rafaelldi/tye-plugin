package com.github.rafaelldi.tyeplugin.api

import com.github.rafaelldi.tyeplugin.api.dto.ApplicationDto
import com.github.rafaelldi.tyeplugin.api.dto.ServiceDto
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

@Service
class TyeApiClient : Disposable {
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
               ignoreUnknownKeys = true
            })     
        }
    }

    suspend fun getApplication(host: Url): ApplicationDto = httpClient.get("$host/api/v1/application")

    suspend fun getServices(host: Url): List<ServiceDto> = httpClient.get("$host/api/v1/services")

    suspend fun controlPlaneShutdown(host: Url) = httpClient.delete<Unit>("$host/api/v1/control")

    override fun dispose() = httpClient.close()
}
