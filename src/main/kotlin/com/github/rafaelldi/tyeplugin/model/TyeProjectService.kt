package com.github.rafaelldi.tyeplugin.model

class TyeProjectService(
    properties: MutableMap<String, String?>,
    bindings: List<TyeServiceBinding>,
    environmentVariables: MutableMap<String, String?>,
    replicas: List<TyeProjectServiceReplica>
) : TyeService(properties, bindings, environmentVariables, replicas) {
    override fun getName(): String = properties[PROPERTY_ID_KEY] ?: "project"
}

class TyeProjectServiceReplica(
    properties: MutableMap<String, String?>,
    environmentVariables: MutableMap<String, String?>?
) : TyeServiceReplica(properties, environmentVariables)