package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ServiceStatusDto(
    val projectFilePath: String?,
    val executablePath: String?,
    val args: String?,
    val workingDirectory: String?
)
