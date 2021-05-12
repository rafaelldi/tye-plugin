package com.github.rafaelldi.tyeplugin.model

class Tye {
    val services: MutableList<Service> = emptyList<Service>().toMutableList()

    fun update(services: List<Service>) {
        this.services.clear()
        this.services.addAll(services)
    }
}
