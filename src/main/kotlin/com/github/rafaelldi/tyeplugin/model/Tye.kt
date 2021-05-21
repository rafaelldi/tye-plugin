package com.github.rafaelldi.tyeplugin.model

class Tye {
    private val services: MutableList<Service> = mutableListOf()

    fun update(services: List<Service>) {
        this.services.clear()
        this.services.addAll(services)
    }

    fun getServices(): List<Service> = services
}