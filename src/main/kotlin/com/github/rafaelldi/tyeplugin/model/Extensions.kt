package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.model.ConfigurationSourceDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceBindingDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceType

fun ServiceDto.toService(): Service {
    val properties = this.toProperties()
    val bindings = description?.bindings?.map { it.toBinding() } ?: emptyList()
    val envVars = description?.configuration?.map { it.toEnvironmentVariable() } ?: emptyList()

    return when (serviceType) {
        ServiceType.External -> Service.External(properties, bindings, envVars)
        ServiceType.Project -> Service.Project(properties, bindings, envVars)
        ServiceType.Executable -> Service.Executable(properties, bindings, envVars)
        ServiceType.Container -> Service.Container(properties, bindings, envVars)
        ServiceType.Function -> Service.Function(properties, bindings, envVars)
        ServiceType.Ingress -> Service.Ingress(properties, bindings, envVars)
    }
}

fun ServiceDto.toProperties(): Properties = Properties(
    description?.name,
    serviceType.toString(),
    description?.replicas,
    restarts,
    description?.runInfo?.project,
    description?.runInfo?.image,
    description?.runInfo?.executable,
    description?.runInfo?.workingDirectory
)

fun ServiceBindingDto.toBinding(): Binding = Binding(
    name,
    connectionString,
    protocol,
    host,
    port,
    containerPort
)

fun ConfigurationSourceDto.toEnvironmentVariable(): EnvironmentVariable = EnvironmentVariable(name, value)
