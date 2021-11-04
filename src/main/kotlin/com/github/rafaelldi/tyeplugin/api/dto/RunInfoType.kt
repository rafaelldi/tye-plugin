package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class RunInfoType {
    @SerialName("project")
    Project,
    @SerialName("executable")
    Executable,
    @SerialName("docker")
    Docker
}
