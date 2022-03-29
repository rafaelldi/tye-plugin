package com.github.rafaelldi.tyeplugin.api.dto

import com.google.gson.annotations.SerializedName

@Suppress("unused")
enum class ServiceSource {
    @SerializedName("unknown")
    Unknown,

    @SerializedName("configuration")
    Configuration,

    @SerializedName("extension")
    Extension,

    @SerializedName("host")
    Host
}