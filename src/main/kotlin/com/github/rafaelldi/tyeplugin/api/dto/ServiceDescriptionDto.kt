package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServiceDescriptionDto(
    val name: String?,
    val replicas: Int,
    val runInfo: RunInfoDto?,
    val bindings: List<ServiceBindingDto>?,
    val configuration: List<ConfigurationSourceDto>?
)
