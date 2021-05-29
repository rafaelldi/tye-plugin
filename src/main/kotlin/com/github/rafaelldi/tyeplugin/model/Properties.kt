package com.github.rafaelldi.tyeplugin.model

data class Properties(
    val id: String?,
    val type: String,
    val replicas: Int?,
    val restarts: Int,
    val project: String?,
    val image: String?,
    val executable: String?,
    val workingDirectory: String?
)
