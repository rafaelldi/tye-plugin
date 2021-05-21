package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.model.ReplicaStatusDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceType

fun ServiceDto.toService(): Service {
    val service = when(this.serviceType){
        ServiceType.External -> Service.External(this.description?.name)
        ServiceType.Project -> Service.Project(this.description?.name)
        ServiceType.Executable -> Service.Executable(this.description?.name)
        ServiceType.Container -> Service.Container(this.description?.name)
        ServiceType.Function -> Service.Function(this.description?.name)
        ServiceType.Ingress -> Service.Ingress(this.description?.name)
    }

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
