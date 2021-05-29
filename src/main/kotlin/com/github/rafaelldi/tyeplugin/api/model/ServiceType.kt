package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ServiceType {
    @SerialName("external")
    External,
    @SerialName("project")
    Project,
    @SerialName("executable")
    Executable,
    @SerialName("container")
    Container,
    @SerialName("function")
    Function,
    @SerialName("ingress")
    Ingress
}
