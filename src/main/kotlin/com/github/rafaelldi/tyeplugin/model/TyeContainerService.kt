package com.github.rafaelldi.tyeplugin.model

class TyeContainerService(
    properties: MutableMap<String, String?>,
    bindings: List<TyeServiceBinding>,
    environmentVariables: MutableMap<String, String?>,
    replicas: List<TyeContainerServiceReplica>
) : TyeService(properties, bindings, environmentVariables, replicas) {
    override fun getName(): String = properties[PROPERTY_ID_KEY] ?: "container"
}

class TyeContainerServiceReplica(
    properties: MutableMap<String, String?>,
    environmentVariables: MutableMap<String, String?>?
) : TyeServiceReplica(properties, environmentVariables)