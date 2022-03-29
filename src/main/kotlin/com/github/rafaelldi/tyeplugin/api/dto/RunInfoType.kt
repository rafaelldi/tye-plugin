package com.github.rafaelldi.tyeplugin.api.dto

import com.google.gson.annotations.SerializedName

@Suppress("unused")
enum class RunInfoType {
    @SerializedName("project")
    Project,
    @SerializedName("executable")
    Executable,
    @SerializedName("docker")
    Docker
}
