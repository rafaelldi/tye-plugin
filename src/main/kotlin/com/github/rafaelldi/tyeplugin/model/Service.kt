package com.github.rafaelldi.tyeplugin.model

class Service(val name: String?){
    private val replicas: MutableList<Replica> = mutableListOf()

    fun getReplicas(): List<Replica> = replicas

    fun addReplicas(replicasToAdd: List<Replica>) {
        replicas.addAll(replicasToAdd)
    }
}
