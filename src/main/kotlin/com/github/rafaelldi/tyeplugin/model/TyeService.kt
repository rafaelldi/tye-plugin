package com.github.rafaelldi.tyeplugin.model

sealed class TyeService(
    val properties: MutableMap<String, String?>,
    val bindings: List<TyeServiceBinding>,
    val environmentVariables: MutableMap<String, String?>?,
    val replicas: List<TyeServiceReplica>?
) {
    companion object {
        const val PROPERTY_ID_KEY = "Id"
        const val PROPERTY_TYPE_KEY = "Type"
        const val PROPERTY_SOURCE_KEY = "Source"
        const val PROPERTY_REPLICAS_KEY = "Replicas"
        const val PROPERTY_RESTARTS_KEY = "Restarts"
        const val PROPERTY_PROJECT_KEY = "Project"
        const val PROPERTY_BUILD_KEY = "Build"
        const val PROPERTY_ARGS_KEY = "Args"
        const val PROPERTY_IMAGE_KEY = "Image"
        const val PROPERTY_EXECUTABLE_KEY = "Executable"
        const val PROPERTY_WORKING_DIRECTORY_KEY = "Working Directory"
    }

    abstract fun getName(): String
}

sealed class TyeServiceReplica(
    val properties: MutableMap<String, String?>,
    val environmentVariables: MutableMap<String, String?>?
) {
    companion object {
        const val REPLICA_PROPERTY_NAME_KEY = "Name"
        const val REPLICA_PROPERTY_STATE_KEY = "State"
        const val REPLICA_PROPERTY_PORTS_KEY = "Ports"
        const val REPLICA_PROPERTY_PID_KEY = "Pid"
        const val REPLICA_PROPERTY_EXIT_CODE_KEY = "Exit Code"
        const val REPLICA_PROPERTY_DOCKER_COMMAND_KEY = "Docker Command"
        const val REPLICA_PROPERTY_CONTAINER_ID_KEY = "Container Id"
        const val REPLICA_PROPERTY_DOCKER_NETWORK_KEY = "Docker Network"
        const val REPLICA_PROPERTY_DOCKER_NETWORK_ALIAS_KEY = "Docker Network Alias"
    }

    fun getName(): String = properties[REPLICA_PROPERTY_NAME_KEY] ?: "replica"
}
