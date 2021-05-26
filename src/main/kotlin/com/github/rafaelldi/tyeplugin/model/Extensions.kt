package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.model.ReplicaStatusDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceType

fun ServiceDto.toService(): Service {
    return when (serviceType) {
        ServiceType.External -> Service.External(description?.name)
        ServiceType.Project -> Service.Project(description?.name)
        ServiceType.Executable -> Service.Executable(description?.name)
        ServiceType.Container -> Service.Container(description?.name)
        ServiceType.Function -> Service.Function(description?.name)
        ServiceType.Ingress -> Service.Ingress(description?.name)
    }
}

fun ReplicaStatusDto.toReplica() = Replica(
    this.name
)
