package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationDto(
    val id: String?,
    val name: String?,
    val source: String?
)
