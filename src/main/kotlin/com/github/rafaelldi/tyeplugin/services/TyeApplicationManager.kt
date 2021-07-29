package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.actions.EditTyeSettingsNotificationAction
import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.github.rafaelldi.tyeplugin.messaging.TyeApplicationNotifier
import com.github.rafaelldi.tyeplugin.model.TyeApplication
import com.github.rafaelldi.tyeplugin.model.toService
import com.github.rafaelldi.tyeplugin.settings.TyeSettingsState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.util.messages.MessageBus
import java.net.ConnectException

@Service
class TyeApplicationManager(private val project: Project) {

    var isConnected: Boolean = false

    private val client: TyeApiClient = service()
    private val messageBus: MessageBus = project.messageBus
    private val application: TyeApplication = TyeApplication()
    private val log = Logger.getInstance(TyeApplicationManager::class.java)

    private lateinit var host: String

    suspend fun connect() {
        val settings = TyeSettingsState.getInstance(project)
        if (settings.tyeHost.isEmpty()) {
            Notification("Tye", "Tye host is empty", "Please specify it", NotificationType.ERROR)
                .addAction(EditTyeSettingsNotificationAction())
                .notify(project)
            return
        }

        host = settings.tyeHost

        try {
            updateApplication()
        } catch (e: ConnectException) {
            Notification(
                "Tye",
                "Cannot connect to tye host",
                "Please check if the address is correct and tye is running",
                NotificationType.ERROR
            )
                .addAction(EditTyeSettingsNotificationAction())
                .notify(project)
            log.debug("Cannot update tye application", e)
            return
        }

        isConnected = true
        val publisher = messageBus.syncPublisher(TyeApplicationNotifier.TOPIC)
        publisher.connectedToTyeHost()
    }

    suspend fun update() {
        if (!isConnected) {
            Notification("Tye", "Please connect to tye", "", NotificationType.ERROR)
                .notify(project)
            return
        }

        try {
            updateApplication()
        } catch (e: ConnectException) {
            Notification(
                "Tye",
                "Cannot connect to tye host",
                "Please check if tye is running",
                NotificationType.ERROR
            )
                .notify(project)
            log.debug("Cannot update tye application", e)
            return
        }

        val publisher = messageBus.syncPublisher(TyeApplicationNotifier.TOPIC)
        publisher.tyeApplicationUpdated()
    }

    private suspend fun updateApplication() {
        val servicesDto = client.getServices(host)
        val services = servicesDto.map { it.toService() }
        application.update(services)
    }

    suspend fun shutdown() {
        if (!isConnected) return

        try {
            client.controlPlaneShutdown(host)
        } catch (e: ConnectException) {
            Notification(
                "Tye",
                "Cannot connect to tye host",
                "Please check if tye is running",
                NotificationType.ERROR
            )
                .notify(project)
            log.debug("Cannot shutdown tye application", e)
            return
        }

        Notification("Tye", "Tye application is stopped", "", NotificationType.INFORMATION)
            .notify(project)

        isConnected = false
        val publisher = messageBus.syncPublisher(TyeApplicationNotifier.TOPIC)
        publisher.tyeApplicationStopped()
    }

    fun getServices(): List<com.github.rafaelldi.tyeplugin.model.Service> = application.getServices()
}
