package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.model.ReplicaStatusDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceDto

fun ServiceDto.toService(): Service {
    val service = Service(this.description?.name)

    val replicas = mutableListOf<Replica>()
    this.replicas?.forEach { replicaMap ->
        replicas.add(replicaMap.value.toReplica())
    }
    service.addReplicas(replicas)

    return service
}

fun ReplicaStatusDto.toReplica() = Replica(
    this.name
)
