package com.github.rafaelldi.tyeplugin.api.dto

data class ServiceStatusDto(
    val projectFilePath: String?,
    val executablePath: String?,
    val args: String?,
    val workingDirectory: String?
)
