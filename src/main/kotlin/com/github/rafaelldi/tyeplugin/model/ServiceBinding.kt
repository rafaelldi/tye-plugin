package com.github.rafaelldi.tyeplugin.model

data class ServiceBinding(
    val name: String?,
    val connectionString: String?,
    val protocol: String?,
    val host: String?,
    val port: Int?,
    val containerPort: Int?
)
