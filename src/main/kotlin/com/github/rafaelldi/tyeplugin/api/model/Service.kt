package com.github.rafaelldi.tyeplugin.api.model

data class Service(
    val Description: ServiceDescription?,
    val ServiceType: ServiceType,
    val Restarts: Int,
    val Status: ServiceStatus?,
    val Replicas: Map<String, ReplicaStatus>?
)
