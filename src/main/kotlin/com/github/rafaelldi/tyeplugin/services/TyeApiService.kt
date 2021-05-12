package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.messaging.TyeServicesNotifier
import com.github.rafaelldi.tyeplugin.model.Tye
import com.github.rafaelldi.tyeplugin.model.toService
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus

@Service
class TyeApiService(project: Project) {

    private val client: TyeApiClient
    private val messageBus: MessageBus

    private var tye: Tye = Tye()

    init {
        val host = "http://localhost:8000"
        client = TyeApiClient(host)
        messageBus = project.messageBus
    }

    suspend fun updateTye() {
        val servicesDto = client.getServices()

        val services = servicesDto.map { it.toService() }
        tye.update(services)

        val publisher = messageBus.syncPublisher(TyeServicesNotifier.TOPIC)
        publisher.servicesUpdated()
    }

    fun getTye(): Tye = tye
}
