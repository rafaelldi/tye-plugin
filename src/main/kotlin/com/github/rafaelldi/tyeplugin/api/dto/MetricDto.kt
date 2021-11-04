package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class MetricDto(
    val name: String?,
    val value: String?,
    val metadata: MetricMetadataDto?
)
