package com.github.rafaelldi.tyeplugin.model

class TyeExecutableService(
    properties: TyeServiceProperties,
    bindings: List<TyeServiceBinding>,
    environmentVariables: List<TyeServiceEnvironmentVariable>
) : TyeService(properties, bindings, environmentVariables) {
    override fun getServiceName(): String = properties.id ?: "unknown"
}