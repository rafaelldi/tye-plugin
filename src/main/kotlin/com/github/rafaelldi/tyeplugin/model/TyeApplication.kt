package com.github.rafaelldi.tyeplugin.model

class TyeApplication(val id: String?, val name: String?, val source: String?) {
    private val services: MutableList<TyeService> = mutableListOf()

    fun updateServices(services: List<TyeService>) {
        this.services.clear()
        this.services.addAll(services)
    }

    fun getServices(): List<TyeService> = services

    fun isServicesEmpty(): Boolean = services.isEmpty()
}
