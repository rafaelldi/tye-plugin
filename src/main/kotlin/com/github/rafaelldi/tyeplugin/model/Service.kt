package com.github.rafaelldi.tyeplugin.model

open class Service(val name: String?) {
    private val replicas: MutableList<Replica> = mutableListOf()

    fun getReplicas(): List<Replica> = replicas

    fun addReplicas(replicasToAdd: List<Replica>) {
        replicas.addAll(replicasToAdd)
    }

    class External(name: String?) : Service(name)

    class Project(name: String?) : Service(name)

    class Executable(name: String?) : Service(name)

    class Container(name: String?) : Service(name)

    class Function(name: String?) : Service(name)

    class Ingress(name: String?) : Service(name)
}
