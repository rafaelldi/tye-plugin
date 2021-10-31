package com.github.rafaelldi.tyeplugin.api.dto

import kotlinx.serialization.Serializable

@Serializable
data class DockerVolumeDto(val name: String?, val source: String?, val target: String?)
