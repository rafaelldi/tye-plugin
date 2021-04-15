package com.github.rafaelldi.tyeplugin.api.model

import kotlinx.serialization.Serializable

@Serializable
enum class ReplicaState {
    Removed,
    Added,
    Started,
    Stopped,
    Healthy,
    Ready
}
