package com.github.rafaelldi.tyeplugin.model

import com.github.rafaelldi.tyeplugin.api.model.ServiceDto

fun ServiceDto.toService() = Service(
    this.description?.name
)
