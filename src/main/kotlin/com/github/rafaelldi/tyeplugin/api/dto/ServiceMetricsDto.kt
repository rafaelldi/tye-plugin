package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServiceMetricsDto(
    val service: String?,
    val metrics: List<MetricDto>?
)