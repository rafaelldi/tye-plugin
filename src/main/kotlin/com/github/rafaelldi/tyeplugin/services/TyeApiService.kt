package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.messaging.TyeServicesNotifier
import com.github.rafaelldi.tyeplugin.model.Tye
import com.github.rafaelldi.tyeplugin.model.toService
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus

@Service
class TyeApiService(private val project: Project) {

    private val client: TyeApiClient = TyeApiClient()
    private val messageBus: MessageBus = project.messageBus
    private val tye: Tye = Tye()

    suspend fun updateTye() {
        try {
            val settings = TyeSettingsState.getInstance(project)
            val servicesDto = client.getServices(settings.tyeHost)
            val services = servicesDto.map { it.toService() }
            tye.update(services)
        } catch (e: Exception) {
            Notification("Tye", "Cannot update service tree", "", NotificationType.ERROR)
                .notify(project)
            return
        }

        val publisher = messageBus.syncPublisher(TyeServicesNotifier.TOPIC)
        publisher.tyeServicesUpdated()
    }

    fun getTye(): Tye = tye
}
