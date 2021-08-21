package com.github.rafaelldi.tyeplugin.model

class TyeExternalService(
    properties: TyeServiceProperties,
    bindings: List<TyeServiceBinding>,
    environmentVariables: List<TyeServiceEnvironmentVariable>
) : TyeService(properties, bindings, environmentVariables) {
    override fun getServiceName(): String {
        TODO("Not yet implemented")
    }
}