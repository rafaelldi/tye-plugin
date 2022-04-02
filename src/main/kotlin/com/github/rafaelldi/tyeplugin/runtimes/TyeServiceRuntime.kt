package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.*
import com.intellij.openapi.vfs.VirtualFile

sealed class TyeServiceRuntime<T>(val service: T, parentRuntime: TyeApplicationRuntime) :
    TyeBaseRuntime(service.getName()) where T : TyeService {
    private val replicaRuntimes: MutableMap<String, TyeReplicaRuntime<TyeServiceReplica>> = mutableMapOf()

    init {
        parent = parentRuntime
    }

    fun update(updatedService: T) {
        updateProperties(updatedService.properties)
        updateEnvironmentVariables(updatedService.environmentVariables)
        updateReplicas(updatedService.replicas ?: emptyList())
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

    private fun updateReplicas(updatedReplicas: List<TyeServiceReplica>) {
        val currentRuntimeNames = replicaRuntimes.keys.toSet()
        val newReplicaNames = updatedReplicas.map { it.getName() }.toSet()

        for (updatedReplica in updatedReplicas) {
            val replicaName = updatedReplica.getName()
            val replicaRuntime = replicaRuntimes[replicaName]
            if (replicaRuntime != null) {
                replicaRuntime.update(updatedReplica)
            } else {
                val newRuntime = createReplicaRuntime(updatedReplica, this)
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

    fun getReplicas(): List<TyeReplicaRuntime<TyeServiceReplica>> = replicaRuntimes.values.toList()

    override fun getSourceFile(): VirtualFile? {
        return null
    }
}