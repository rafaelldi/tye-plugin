package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceDto(
    val description: ServiceDescriptionDto?,
    val serviceType: ServiceType,
    val restarts: Int,
    val status: ServiceStatusDto?,
    val replicas: Map<String, ReplicaStatusDto>?
)
