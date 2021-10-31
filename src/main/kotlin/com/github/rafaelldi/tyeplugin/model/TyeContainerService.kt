package com.github.rafaelldi.tyeplugin.model

class TyeContainerService(
    properties: TyeContainerServiceProperties,
    bindings: List<TyeServiceBinding>,
    environmentVariables: List<TyeEnvironmentVariable>,
    replicas: List<TyeContainerServiceReplica>
) : TyeService(properties, bindings, environmentVariables, replicas) {
    override fun getServiceName(): String = properties.id ?: "unknown"
}

class TyeContainerServiceProperties(
    id: String?,
    type: String,
    source: String,
    replicas: Int?,
    restarts: Int,
    val image: String?,
    val args: String?
) : TyeServiceProperties(id, type, source, replicas, restarts)

class TyeContainerServiceReplica(
    name: String,
    state: TyeReplicaState?,
    ports: List<Int>?,
    environmentVariables: List<TyeEnvironmentVariable>?,
    val dockerCommand: String?,
    val containerId: String?,
    val dockerNetwork: String?,
    val dockerNetworkAlias: String?
) : TyeServiceReplica(name, state, ports, environmentVariables)