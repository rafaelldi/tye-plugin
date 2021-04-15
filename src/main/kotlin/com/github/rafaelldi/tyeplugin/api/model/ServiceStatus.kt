package com.github.rafaelldi.tyeplugin.api.model

data class ServiceStatus(
    val ProjectFilePath: String?,
    val ExecutablePath: String?,
    val Args: String?,
    val WorkingDirectory: String?
)
