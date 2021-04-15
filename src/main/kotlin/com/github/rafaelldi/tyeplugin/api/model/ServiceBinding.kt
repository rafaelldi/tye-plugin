package com.github.rafaelldi.tyeplugin.api.model

data class ServiceBinding(
    val Name: String?,
    val ConnectionString: String?,
    val AutoAssignPort: Boolean,
    val Port: Int?,
    val ContainerPort: Int?,
    val Host: String?,
    val Protocol: String?
)
