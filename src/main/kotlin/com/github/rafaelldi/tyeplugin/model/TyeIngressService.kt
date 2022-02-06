package com.github.rafaelldi.tyeplugin.model

class TyeIngressService(
    properties: TyeIngressServiceProperties,
    bindings: List<TyeServiceBinding>,
    replicas: List<TyeIngressServiceReplica>
) : TyeService(properties, bindings, null, replicas){
    override fun getServiceName(): String = properties.id ?: "ingress"
}

class TyeIngressServiceProperties(
    id: String?,
    type: String,
    source: String,
    replicas: Int?,
    restarts: Int
) : TyeServiceProperties(id, type, source, replicas, restarts)

class TyeIngressServiceReplica(
    name: String,
    state: TyeReplicaState?,
    ports: List<Int>?
) : TyeServiceReplica(name, state, ports, null)