package com.github.rafaelldi.tyeplugin.api.dto

data class ServiceDescriptionDto(
    val name: String?,
    val replicas: Int,
    val runInfo: RunInfoDto?,
    val bindings: List<ServiceBindingDto>?,
    val configuration: List<ConfigurationSourceDto>?
)
