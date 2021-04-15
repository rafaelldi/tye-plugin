package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
enum class RunInfoType {
    Project,
    Executable,
    Docker
}
