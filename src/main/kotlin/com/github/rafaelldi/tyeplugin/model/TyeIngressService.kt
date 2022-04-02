package com.github.rafaelldi.tyeplugin.model

class TyeIngressService(
    properties: MutableMap<String, String?>,
    bindings: List<TyeServiceBinding>,
    replicas: List<TyeIngressServiceReplica>
) : TyeService(properties, bindings, null, replicas) {
    override fun getName(): String = properties[PROPERTY_ID_KEY] ?: "ingress"
}

class TyeIngressServiceReplica(
    properties: MutableMap<String, String?>
) : TyeServiceReplica(properties, null)