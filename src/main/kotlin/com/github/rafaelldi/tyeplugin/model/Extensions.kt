package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.model.ServiceDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceType

fun ServiceDto.toService(): Service {
    val properties = Properties(description?.name, serviceType.toString(), description?.replicas, restarts)
    return when (serviceType) {
        ServiceType.External -> Service.External(properties)
        ServiceType.Project -> Service.Project(properties)
        ServiceType.Executable -> Service.Executable(properties)
        ServiceType.Container -> Service.Container(properties)
        ServiceType.Function -> Service.Function(properties)
        ServiceType.Ingress -> Service.Ingress(properties)
    }
}
