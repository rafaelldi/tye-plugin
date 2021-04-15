package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ReplicaStatus(
    val dockerCommand: String?,
    val containerId: String?,
    val dockerNetwork: String?,
    val dockerNetworkAlias: String?,
    val name: String?,
    val ports: List<Int>?,
    val exitCode: Int?,
    val pid: Int?,
    val environment: Map<String, String>?,
    val state: ReplicaState?
)
