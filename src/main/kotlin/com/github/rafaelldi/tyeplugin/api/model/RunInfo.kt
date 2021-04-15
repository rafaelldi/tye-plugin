package com.github.rafaelldi.tyeplugin.api.model

data class RunInfo(
    val Type: RunInfoType,
    val Args: String?,
    val Build: Boolean,
    val Project: String?,
    val WorkingDirectory: String?,
    val Image: String?,
    val Executable: String?,
    val VolumeMappings: List<DockerVolume>?
)
