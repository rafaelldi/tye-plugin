package com.github.rafaelldi.tyeplugin.model

sealed class TyeService(
    val properties: TyeServiceProperties,
    val bindings: List<TyeServiceBinding>,
    val environmentVariables: List<TyeEnvironmentVariable>?,
    val replicas: List<TyeServiceReplica>?
) {
    abstract fun getServiceName(): String
}

sealed class TyeServiceProperties(
    val id: String?,
    val type: String,
    val source: String,
    val replicas: Int?,
    val restarts: Int
)

sealed class TyeServiceReplica(
    val name: String,
    val state: TyeReplicaState?,
    val ports: List<Int>?,
    val environmentVariables: List<TyeEnvironmentVariable>?
)
