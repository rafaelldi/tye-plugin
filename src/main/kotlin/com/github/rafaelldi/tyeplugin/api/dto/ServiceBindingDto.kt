package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServiceBindingDto(
    val name: String?,
    val connectionString: String?,
    val autoAssignPort: Boolean,
    val port: Int?,
    val containerPort: Int?,
    val host: String?,
    val protocol: String?
)
