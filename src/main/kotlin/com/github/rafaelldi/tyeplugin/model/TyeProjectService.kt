package com.github.rafaelldi.tyeplugin.model

class TyeProjectService(
    properties: TyeProjectServiceProperties,
    bindings: List<TyeServiceBinding>,
    environmentVariables: List<TyeEnvironmentVariable>,
    replicas: List<TyeProjectServiceReplica>
) : TyeService(properties, bindings, environmentVariables, replicas) {
    override fun getServiceName(): String = properties.id ?: "project"
}

class TyeProjectServiceProperties(
    id: String?,
    type: String,
    source: String,
    replicas: Int?,
    restarts: Int,
    val project: String?,
    val build: Boolean?,
    val args: String?
) : TyeServiceProperties(id, type, source, replicas, restarts)

class TyeProjectServiceReplica(
    name: String,
    state: TyeReplicaState?,
    ports: List<Int>?,
    environmentVariables: List<TyeEnvironmentVariable>?,
    val pid: Int?,
    val exitCode: Int?
) : TyeServiceReplica(name, state, ports, environmentVariables)