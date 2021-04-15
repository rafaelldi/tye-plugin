package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Service(
    val description: ServiceDescription?,
    val serviceType: ServiceType,
    val restarts: Int,
    val status: ServiceStatus?,
    val replicas: Map<String, ReplicaStatus>?
)
