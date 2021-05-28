package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.model.ServiceBindingDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceType

fun ServiceDto.toService(): Service {
    val properties = Properties(description?.name, serviceType.toString(), description?.replicas, restarts)
    val serviceBindings = description?.bindings?.map { it.toServiceBinding() } ?: emptyList()
    return when (serviceType) {
        ServiceType.External -> Service.External(properties, serviceBindings)
        ServiceType.Project -> Service.Project(properties, serviceBindings)
        ServiceType.Executable -> Service.Executable(properties, serviceBindings)
        ServiceType.Container -> Service.Container(properties, serviceBindings)
        ServiceType.Function -> Service.Function(properties, serviceBindings)
        ServiceType.Ingress -> Service.Ingress(properties, serviceBindings)
    }
}

fun ServiceBindingDto.toServiceBinding(): ServiceBinding = ServiceBinding(
    name,
    connectionString,
    protocol,
    host,
    port,
    containerPort
)
