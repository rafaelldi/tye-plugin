package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class RunInfoDto(
    val type: RunInfoType,
    val args: String?,
    val build: Boolean,
    val project: String?,
    val workingDirectory: String?,
    val image: String?,
    val executable: String?,
    val volumeMappings: List<DockerVolumeDto>?
)
