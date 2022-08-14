package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.*
import com.intellij.openapi.vfs.VirtualFile

sealed class TyeServiceRuntime<T>(val service: T, parentRuntime: TyeApplicationRuntime) :
    TyeBaseRuntime(service.getName()) where T : TyeService {
    private val replicaRuntimes: MutableMap<String, TyeReplicaRuntime<TyeServiceReplica>> = mutableMapOf()

    init {
        parent = parentRuntime
    }

    fun update(service: T) {
        updateProperties(service.properties)
        updateEnvironmentVariables(service.environmentVariables)
        updateReplicas(service.replicas ?: emptyList())
    }

    private fun updateProperties(updatedProperties: MutableMap<String, String?>) {
        val removedProperties = service.properties.keys.subtract(updatedProperties.keys)

        for (property in updatedProperties) {
            val currentValue = service.properties[property.key]
            if (currentValue == property.value) {
                continue
            }

            service.properties[property.key] = property.value
        }

        for (removedProperty in removedProperties) {
            service.properties.remove(removedProperty)
        }
    }

    private fun updateEnvironmentVariables(updatedVariables: MutableMap<String, String?>?) {
        if (updatedVariables == null || service.environmentVariables == null) {
            return
        }

        val removedVariables = service.environmentVariables.keys.subtract(updatedVariables.keys)

        for (variable in updatedVariables) {
            val currentValue = service.environmentVariables[variable.key]
            if (currentValue == variable.value) {
                continue
            }

            service.environmentVariables[variable.key] = variable.value
        }

        for (removedVariable in removedVariables) {
            service.environmentVariables.remove(removedVariable)
        }
    }

    private fun updateReplicas(replicas: List<TyeServiceReplica>) {
        val currentRuntimeNames = replicaRuntimes.keys.toSet()
        val newReplicaNames = replicas.map { it.getName() }.toSet()

        for (replica in replicas) {
            val replicaName = replica.getName()
            val replicaRuntime = replicaRuntimes[replicaName]
            if (replicaRuntime != null) {
                replicaRuntime.update(replica)
            } else {
                val newRuntime = createReplicaRuntime(replica, this)
                replicaRuntimes[replicaName] = newRuntime
            }
        }

        for (deletedRuntimeName in currentRuntimeNames.subtract(newReplicaNames)) {
            replicaRuntimes.remove(deletedRuntimeName)
        }
    }

    private fun createReplicaRuntime(
        model: TyeServiceReplica,
        parent: TyeServiceRuntime<*>
    ): TyeReplicaRuntime<TyeServiceReplica> =
        when (model) {
            is TyeContainerServiceReplica -> TyeReplicaRuntime(model, parent)
            is TyeProjectServiceReplica -> TyeReplicaRuntime(model, parent)
            is TyeIngressServiceReplica -> TyeReplicaRuntime(model, parent)
        }

    fun getRuntimes(): List<TyeReplicaRuntime<TyeServiceReplica>> = replicaRuntimes.values.toList()

    fun clearRuntimes() {
        replicaRuntimes.forEach { it.value.removeDeployment() }
        replicaRuntimes.clear()
    }

    override fun getSourceFile(): VirtualFile? {
        return null
    }

    fun getServiceUrl(): String? = replicaRuntimes.values.firstOrNull()?.getReplicaUrl()
}