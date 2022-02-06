package com.github.rafaelldi.tyeplugin.model

class TyeExecutableService(
    properties: TyeExecutableServiceProperties,
    bindings: List<TyeServiceBinding>,
    environmentVariables: List<TyeEnvironmentVariable>
) : TyeService(properties, bindings, environmentVariables, null) {
    override fun getServiceName(): String = properties.id ?: "executable"
}

class TyeExecutableServiceProperties(
    id: String?,
    type: String,
    source: String,
    replicas: Int?,
    restarts: Int,
    val executable: String?,
    val workingDirectory: String?,
    val args: String?
) : TyeServiceProperties(id, type, source, replicas, restarts)