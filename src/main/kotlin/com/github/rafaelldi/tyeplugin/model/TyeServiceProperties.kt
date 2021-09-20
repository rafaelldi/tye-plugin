package com.github.rafaelldi.tyeplugin.model

data class TyeServiceProperties(
    val id: String?,
    val type: String,
    val source: String,
    val replicas: Int?,
    val restarts: Int,
    val project: String?,
    val image: String?,
    val executable: String?,
    val workingDirectory: String?
)