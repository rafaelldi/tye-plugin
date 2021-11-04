package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ServiceSource {
    @SerialName("unknown")
    Unknown,

    @SerialName("configuration")
    Configuration,

    @SerialName("extension")
    Extension,

    @SerialName("host")
    Host
}