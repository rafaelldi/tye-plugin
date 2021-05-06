package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ReplicaState {
    @SerialName("removed")
    Removed,
    @SerialName("added")
    Added,
    @SerialName("started")
    Started,
    @SerialName("stopped")
    Stopped,
    @SerialName("healthy")
    Healthy,
    @SerialName("ready")
    Ready
}
