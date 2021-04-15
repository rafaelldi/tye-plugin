package com.github.rafaelldi.tyeplugin.api.model

data class ServiceDescription(
    val Name: String?,
    val Replicas: Int,
    val RunInfo: RunInfo?,
    val Bindings: List<ServiceBinding>?,
    val Configuration: List<ConfigurationSource>?
)
