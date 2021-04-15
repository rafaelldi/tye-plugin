package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceBinding(
    val name: String?,
    val connectionString: String?,
    val autoAssignPort: Boolean,
    val port: Int?,
    val containerPort: Int?,
    val host: String?,
    val protocol: String?
)
