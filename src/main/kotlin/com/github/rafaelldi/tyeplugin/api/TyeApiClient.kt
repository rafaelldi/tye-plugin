package com.github.rafaelldi.tyeplugin.api

import com.github.rafaelldi.tyeplugin.api.dto.ApplicationDto
import com.github.rafaelldi.tyeplugin.api.dto.ServiceDto
import com.github.rafaelldi.tyeplugin.api.dto.ServiceMetricsDto
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

@Service
class TyeApiClient : Disposable {
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun getApplication(host: Url): ApplicationDto = client.get("$host/api/v1/application").body()

    suspend fun getServices(host: Url): List<ServiceDto> = client.get("$host/api/v1/services").body()

    suspend fun getMetrics(host: Url): List<ServiceMetricsDto> = client.get("$host/api/v1/metrics").body()

    suspend fun controlPlaneShutdown(host: Url) = client.delete("$host/api/v1/control")

    override fun dispose() = client.close()
}
