package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.dto.*

fun ApplicationDto.toModel(): TyeApplication = TyeApplication(id, name, source)

fun ServiceDto.toModel(): TyeService? {
    val bindings = description?.bindings?.map { it.toBinding() } ?: emptyList()
    val envVars = description?.configuration?.map { it.toEnvironmentVariable() } ?: emptyList()

    return when (serviceType) {
        ServiceType.Project -> TyeProjectService(
            this.toProjectProperties(),
            bindings,
            envVars,
            this.replicas?.toProjectReplicas() ?: emptyList()
        )
        ServiceType.Executable -> TyeExecutableService(
            this.toExecutableProperties(),
            bindings,
            envVars
        )
        ServiceType.Container -> TyeContainerService(
            this.toContainerProperties(),
            bindings,
            envVars,
            this.replicas?.toContainerReplicas() ?: emptyList()
        )
        else -> null
    }
}

private fun ServiceDto.toProjectProperties(): TyeProjectServiceProperties = TyeProjectServiceProperties(
    description?.name,
    serviceType.toString(),
    serviceSource.toString(),
    description?.replicas,
    restarts,
    description?.runInfo?.project,
    description?.runInfo?.build,
    description?.runInfo?.args
)

private fun ServiceDto.toContainerProperties(): TyeContainerServiceProperties = TyeContainerServiceProperties(
    description?.name,
    serviceType.toString(),
    serviceSource.toString(),
    description?.replicas,
    restarts,
    description?.runInfo?.image,
    description?.runInfo?.args
)

private fun ServiceDto.toExecutableProperties(): TyeExecutableServiceProperties = TyeExecutableServiceProperties(
    description?.name,
    serviceType.toString(),
    serviceSource.toString(),
    description?.replicas,
    restarts,
    description?.runInfo?.executable,
    description?.runInfo?.workingDirectory,
    description?.runInfo?.args
)

private fun ServiceBindingDto.toBinding(): TyeServiceBinding = TyeServiceBinding(
    name,
    connectionString,
    protocol,
    host,
    port,
    containerPort
)

private fun Map<String, ReplicaStatusDto>.toProjectReplicas(): List<TyeProjectServiceReplica> = this.map {
    TyeProjectServiceReplica(
        it.key,
        it.value.state?.toState(),
        it.value.ports,
        it.value.environment?.toEnvironmentVariables(),
        it.value.pid,
        it.value.exitCode
    )
}

private fun Map<String, ReplicaStatusDto>.toContainerReplicas(): List<TyeContainerServiceReplica> = this.map {
    TyeContainerServiceReplica(
        it.key,
        it.value.state?.toState(),
        it.value.ports,
        it.value.environment?.toEnvironmentVariables(),
        it.value.dockerCommand,
        it.value.containerId,
        it.value.dockerNetwork,
        it.value.dockerNetworkAlias
    )
}

private fun ReplicaState.toState(): TyeReplicaState = when (this) {
    ReplicaState.Removed -> TyeReplicaState.Removed
    ReplicaState.Added -> TyeReplicaState.Added
    ReplicaState.Started -> TyeReplicaState.Started
    ReplicaState.Stopped -> TyeReplicaState.Stopped
    ReplicaState.Healthy -> TyeReplicaState.Healthy
    ReplicaState.Ready -> TyeReplicaState.Ready
}

private fun ConfigurationSourceDto.toEnvironmentVariable(): TyeEnvironmentVariable =
    TyeEnvironmentVariable(name ?: "", value)

private fun Map<String, String>.toEnvironmentVariables(): List<TyeEnvironmentVariable> = this.map {
    TyeEnvironmentVariable(it.key, it.value)
}