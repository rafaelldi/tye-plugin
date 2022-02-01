package com.github.rafaelldi.tyeplugin.api.dto

data class ReplicaStatusDto(
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
