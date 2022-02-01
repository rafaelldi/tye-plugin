package com.github.rafaelldi.tyeplugin.api.dto

data class ServiceDto(
    val description: ServiceDescriptionDto?,
    val serviceType: ServiceType,
    val serviceSource: ServiceSource,
    val restarts: Int,
    val status: ServiceStatusDto?,
    val replicas: Map<String, ReplicaStatusDto>?
)
