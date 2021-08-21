package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.model.ConfigurationSourceDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceBindingDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceDto
import com.github.rafaelldi.tyeplugin.api.model.ServiceType

fun ServiceDto.toService(): TyeService {
    val properties = this.toProperties()
    val bindings = description?.bindings?.map { it.toBinding() } ?: emptyList()
    val envVars = description?.configuration?.map { it.toEnvironmentVariable() } ?: emptyList()

    return when (serviceType) {
        ServiceType.External -> TyeExternalService(properties, bindings, envVars)
        ServiceType.Project -> TyeProjectService(properties, bindings, envVars)
        ServiceType.Executable -> TyeExecutableService(properties, bindings, envVars)
        ServiceType.Container -> TyeContainerService(properties, bindings, envVars)
        ServiceType.Function -> TyeFunctionService(properties, bindings, envVars)
        ServiceType.Ingress -> TyeIngressService(properties, bindings, envVars)
    }
}

fun ServiceDto.toProperties(): TyeServiceProperties = TyeServiceProperties(
    description?.name,
    serviceType.toString(),
    description?.replicas,
    restarts,
    description?.runInfo?.project,
    description?.runInfo?.image,
    description?.runInfo?.executable,
    description?.runInfo?.workingDirectory
)

fun ServiceBindingDto.toBinding(): TyeServiceBinding = TyeServiceBinding(
    name,
    connectionString,
    protocol,
    host,
    port,
    containerPort
)

fun ConfigurationSourceDto.toEnvironmentVariable(): TyeServiceEnvironmentVariable = TyeServiceEnvironmentVariable(name, value)
