package com.github.rafaelldi.tyeplugin.api

import com.github.rafaelldi.tyeplugin.api.model.ServiceDto
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.delete
import io.ktor.client.request.get

@Service
class TyeApiClient : Disposable {
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getServices(host: String): List<ServiceDto> = httpClient.get("$host/api/v1/services")

    suspend fun controlPlaneShutdown(host: String) = httpClient.delete<Unit>("$host/api/v1/control")

    override fun dispose() = httpClient.close()
}
