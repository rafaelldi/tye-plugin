package com.github.rafaelldi.tyeplugin.api.dto

data class MetricDto(
    val name: String?,
    val value: String?,
    val metadata: MetricMetadataDto?
)
