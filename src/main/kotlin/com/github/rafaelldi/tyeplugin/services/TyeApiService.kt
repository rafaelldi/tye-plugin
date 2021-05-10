package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.listeners.TyeServicesNotifier
import com.github.rafaelldi.tyeplugin.model.TyeService
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Service
class TyeApiService(project: Project) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val client: TyeApiClient
    private val messageBus: MessageBus

    private var tyeServices: List<TyeService> = emptyList()

    init {
        val host = "http://localhost:8000"
        client = TyeApiClient(host)
        messageBus = project.messageBus
    }

    fun updateServices() {
        scope.launch {
            val services = client.getServices()
            tyeServices = services.map { TyeService(it.description?.name) }
        }

        val publisher = messageBus.syncPublisher(TyeServicesNotifier.TOPIC)
        publisher.servicesUpdated()
    }

    fun getServices() : List<TyeService> = tyeServices
}