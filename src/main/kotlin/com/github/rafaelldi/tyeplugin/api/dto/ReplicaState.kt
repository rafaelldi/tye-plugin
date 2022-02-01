package com.github.rafaelldi.tyeplugin.api.dto

import com.google.gson.annotations.SerializedName

enum class ReplicaState {
    @SerializedName("removed")
    Removed,
    @SerializedName("added")
    Added,
    @SerializedName("started")
    Started,
    @SerializedName("stopped")
    Stopped,
    @SerializedName("healthy")
    Healthy,
    @SerializedName("ready")
    Ready
}
