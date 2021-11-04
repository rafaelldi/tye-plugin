package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationSourceDto(val name: String?, val value: String?)
