package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceDescription(
    val name: String?,
    val replicas: Int,
    val runInfo: RunInfo?,
    val bindings: List<ServiceBinding>?,
    val configuration: List<ConfigurationSource>?
)
