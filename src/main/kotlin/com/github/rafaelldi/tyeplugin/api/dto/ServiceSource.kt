package com.github.rafaelldi.tyeplugin.api.dto

import com.google.gson.annotations.SerializedName

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