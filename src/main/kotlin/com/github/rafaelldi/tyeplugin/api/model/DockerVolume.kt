package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
data class DockerVolume(val name: String?, val source: String?, val target: String?)
