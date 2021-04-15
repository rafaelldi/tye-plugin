package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
enum class ServiceType {
    External,
    Project,
    Executable,
    Container,
    Function,
    Ingress
}
