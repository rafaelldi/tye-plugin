package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.actions.EditSettingsNotificationAction
import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.messaging.TyeServicesNotifier
import com.github.rafaelldi.tyeplugin.model.Tye
import com.github.rafaelldi.tyeplugin.model.toService
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus
import java.net.ConnectException

@Service
class TyeApiService(private val project: Project) {

    private val client: TyeApiClient = TyeApiClient()
    private val messageBus: MessageBus = project.messageBus
    private val tye: Tye = Tye()
    private val log = Logger.getInstance(TyeApiService::class.java)

    suspend fun updateTye() {
        try {
            val settings = TyeSettingsState.getInstance(project)
            val servicesDto = client.getServices(settings.tyeHost)
            val services = servicesDto.map { it.toService() }
            tye.update(services)
        } catch (e: ConnectException) {
            Notification(
                "Tye",
                "Cannot connect to tye host",
                "Please check if the address is correct",
                NotificationType.ERROR
            )
                .addAction(EditSettingsNotificationAction())
                .notify(project)
            log.error("Cannot update service tree", e)
            return
        }

        val publisher = messageBus.syncPublisher(TyeServicesNotifier.TOPIC)
        publisher.tyeServicesUpdated()
    }

    fun getTye(): Tye = tye
}
