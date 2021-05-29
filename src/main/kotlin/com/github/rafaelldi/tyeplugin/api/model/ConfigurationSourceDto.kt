package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ConfigurationSourceDto(val name: String?, val value: String?)
