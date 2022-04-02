package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.dto.*
 import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_ARGS_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_BUILD_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_EXECUTABLE_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_ID_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_IMAGE_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_PROJECT_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_REPLICAS_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_RESTARTS_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_SOURCE_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_TYPE_KEY
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_WORKING_DIRECTORY_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_CONTAINER_ID_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_DOCKER_COMMAND_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_DOCKER_NETWORK_ALIAS_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_DOCKER_NETWORK_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_EXIT_CODE_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_NAME_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_PID_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_PORTS_KEY
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_STATE_KEY

fun ApplicationDto.toModel(): TyeApplication = TyeApplication(id, name, source)

fun ServiceDto.toModel(): TyeService? {
    val bindings = description?.bindings?.map { it.toBinding() } ?: emptyList()
    val envVars = description?.configuration
        ?.filter { !it.name.isNullOrEmpty() }
        ?.associate { it.name!! to it.value }
        ?.toMutableMap()
        ?: mutableMapOf()

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
        ServiceType.Ingress -> TyeIngressService(
            this.toIngressProperties(),
            bindings,
            this.replicas?.toIngressReplicas() ?: emptyList()
        )
        else -> null
    }
}


private fun ServiceDto.toProjectProperties(): MutableMap<String, String?> =
    mutableMapOf(
        PROPERTY_ID_KEY to description?.name,
        PROPERTY_TYPE_KEY to serviceType.toString(),
        PROPERTY_SOURCE_KEY to serviceSource.toString(),
        PROPERTY_REPLICAS_KEY to description?.replicas?.toString(),
        PROPERTY_RESTARTS_KEY to restarts.toString(),
        PROPERTY_PROJECT_KEY to description?.runInfo?.project,
        PROPERTY_BUILD_KEY to description?.runInfo?.build?.toString(),
        PROPERTY_ARGS_KEY to description?.runInfo?.args
    )

private fun ServiceDto.toContainerProperties(): MutableMap<String, String?> =
    mutableMapOf(
        PROPERTY_ID_KEY to description?.name,
        PROPERTY_TYPE_KEY to serviceType.toString(),
        PROPERTY_SOURCE_KEY to serviceSource.toString(),
        PROPERTY_REPLICAS_KEY to description?.replicas?.toString(),
        PROPERTY_RESTARTS_KEY to restarts.toString(),
        PROPERTY_IMAGE_KEY to description?.runInfo?.image,
        PROPERTY_ARGS_KEY to description?.runInfo?.args
    )

private fun ServiceDto.toExecutableProperties(): MutableMap<String, String?> =
    mutableMapOf(
        PROPERTY_ID_KEY to description?.name,
        PROPERTY_TYPE_KEY to serviceType.toString(),
        PROPERTY_SOURCE_KEY to serviceSource.toString(),
        PROPERTY_REPLICAS_KEY to description?.replicas?.toString(),
        PROPERTY_RESTARTS_KEY to restarts.toString(),
        PROPERTY_EXECUTABLE_KEY to description?.runInfo?.executable,
        PROPERTY_IMAGE_KEY to description?.runInfo?.workingDirectory,
        PROPERTY_WORKING_DIRECTORY_KEY to description?.runInfo?.args
    )

private fun ServiceDto.toIngressProperties(): MutableMap<String, String?> =
    mutableMapOf(
        PROPERTY_ID_KEY to description?.name,
        PROPERTY_TYPE_KEY to serviceType.toString(),
        PROPERTY_SOURCE_KEY to serviceSource.toString(),
        PROPERTY_REPLICAS_KEY to description?.replicas?.toString(),
        PROPERTY_RESTARTS_KEY to restarts.toString()
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
        it.value.toProjectReplicaProperties(it.key),
        it.value.environment?.toMutableMap()
    )
}

private fun ReplicaStatusDto.toProjectReplicaProperties(name: String): MutableMap<String, String?> =
    mutableMapOf(
        REPLICA_PROPERTY_NAME_KEY to name,
        REPLICA_PROPERTY_STATE_KEY to state.toString(),
        REPLICA_PROPERTY_PORTS_KEY to ports?.joinToString(),
        REPLICA_PROPERTY_PID_KEY to pid?.toString(),
        REPLICA_PROPERTY_EXIT_CODE_KEY to exitCode?.toString()
    )

private fun Map<String, ReplicaStatusDto>.toContainerReplicas(): List<TyeContainerServiceReplica> = this.map {
    TyeContainerServiceReplica(
        it.value.toContainerReplicaProperties(it.key),
        it.value.environment?.toMutableMap()
    )
}

private fun ReplicaStatusDto.toContainerReplicaProperties(name: String): MutableMap<String, String?> =
    mutableMapOf(
        REPLICA_PROPERTY_NAME_KEY to name,
        REPLICA_PROPERTY_STATE_KEY to state.toString(),
        REPLICA_PROPERTY_PORTS_KEY to ports?.joinToString(),
        REPLICA_PROPERTY_DOCKER_COMMAND_KEY to dockerCommand,
        REPLICA_PROPERTY_CONTAINER_ID_KEY to containerId,
        REPLICA_PROPERTY_DOCKER_NETWORK_KEY to dockerNetwork,
        REPLICA_PROPERTY_DOCKER_NETWORK_ALIAS_KEY to dockerNetworkAlias
    )

private fun Map<String, ReplicaStatusDto>.toIngressReplicas(): List<TyeIngressServiceReplica> = this.map {
    TyeIngressServiceReplica(
        it.value.toIngressReplicaProperties(it.key)
    )
}

private fun ReplicaStatusDto.toIngressReplicaProperties(name: String): MutableMap<String, String?> =
    mutableMapOf(
        REPLICA_PROPERTY_NAME_KEY to name,
        REPLICA_PROPERTY_STATE_KEY to state.toString(),
        REPLICA_PROPERTY_PORTS_KEY to ports?.joinToString(),
    )