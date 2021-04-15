package com.github.rafaelldi.tyeplugin.api.model

data class ReplicaStatus(
    val DockerCommand: String?,
    val ContainerId: String?,
    val DockerNetwork: String?,
    val DockerNetworkAlias: String?,
    val Name: String?,
    val Ports: List<Int>?,
    val ExitCode: Int?,
    val Pid: Int?,
    val Environment: Map<String, String>?,
    val State: ReplicaState?
)
