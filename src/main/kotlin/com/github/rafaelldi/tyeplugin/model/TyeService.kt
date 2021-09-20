package com.github.rafaelldi.tyeplugin.model

sealed class TyeService(
    val properties: TyeServiceProperties,
    val bindings: List<TyeServiceBinding>,
    val environmentVariables: List<TyeServiceEnvironmentVariable>
) {
    abstract fun getServiceName(): String
}
