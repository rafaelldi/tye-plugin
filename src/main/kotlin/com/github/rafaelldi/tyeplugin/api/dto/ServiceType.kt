package com.github.rafaelldi.tyeplugin.api.dto

import com.google.gson.annotations.SerializedName

enum class ServiceType {
    @SerializedName("external")
    External,
    @SerializedName("project")
    Project,
    @SerializedName("executable")
    Executable,
    @SerializedName("container")
    Container,
    @SerializedName("function")
    Function,
    @SerializedName("ingress")
    Ingress
}
