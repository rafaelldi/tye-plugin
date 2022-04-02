package com.github.rafaelldi.tyeplugin.model

sealed class TyeService(
    val properties: MutableMap<String, String?>,
    val bindings: List<TyeServiceBinding>,
    val environmentVariables: MutableMap<String, String?>?,
    val replicas: List<TyeServiceReplica>?
) {
    companion object {
        const val PROPERTY_ID_KEY = "id"
        const val PROPERTY_TYPE_KEY = "type"
        const val PROPERTY_SOURCE_KEY = "source"
        const val PROPERTY_REPLICAS_KEY = "replicas"
        const val PROPERTY_RESTARTS_KEY = "restarts"
        const val PROPERTY_PROJECT_KEY = "project"
        const val PROPERTY_BUILD_KEY = "build"
        const val PROPERTY_ARGS_KEY = "args"
        const val PROPERTY_IMAGE_KEY = "image"
        const val PROPERTY_EXECUTABLE_KEY = "executable"
        const val PROPERTY_WORKING_DIRECTORY_KEY = "working directory"
    }

    abstract fun getName(): String
}

sealed class TyeServiceReplica(
    val properties: MutableMap<String, String?>,
    val environmentVariables: MutableMap<String, String?>?
) {
    companion object {
        const val REPLICA_PROPERTY_NAME_KEY = "name"
        const val REPLICA_PROPERTY_STATE_KEY = "state"
        const val REPLICA_PROPERTY_PORTS_KEY = "ports"
        const val REPLICA_PROPERTY_PID_KEY = "pid"
        const val REPLICA_PROPERTY_EXIT_CODE_KEY = "exit code"
        const val REPLICA_PROPERTY_DOCKER_COMMAND_KEY = "docker command"
        const val REPLICA_PROPERTY_CONTAINER_ID_KEY = "container id"
        const val REPLICA_PROPERTY_DOCKER_NETWORK_KEY = "docker network"
        const val REPLICA_PROPERTY_DOCKER_NETWORK_ALIAS_KEY = "docker network alias"
    }

    fun getName(): String = properties[REPLICA_PROPERTY_NAME_KEY] ?: "replica"
}
