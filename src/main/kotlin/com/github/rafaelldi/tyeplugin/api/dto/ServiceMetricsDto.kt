package com.github.rafaelldi.tyeplugin.api.dto

data class ServiceMetricsDto(
    val service: String?,
    val metrics: List<MetricDto>?
)