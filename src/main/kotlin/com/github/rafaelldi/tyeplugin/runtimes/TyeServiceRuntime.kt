package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeContainerServiceReplica
import com.github.rafaelldi.tyeplugin.model.TyeProjectServiceReplica
import com.github.rafaelldi.tyeplugin.model.TyeService
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica
import com.intellij.openapi.vfs.VirtualFile

sealed class TyeServiceRuntime<T>(val service: T, parentRuntime: TyeApplicationRuntime) :
    TyeBaseRuntime(service.getServiceName()) where T : TyeService {

    private val replicaRuntimes: MutableMap<String, TyeReplicaRuntime<TyeServiceReplica>> = mutableMapOf()

    init {
        parent = parentRuntime
    }

    fun updateReplicas(service: T) {
        val currentRuntimeNames = replicaRuntimes.keys.toSet()
        val newReplicaNames = service.replicas?.map { it.name }?.toSet() ?: emptySet()

        for (newReplica in service.replicas ?: emptyList()) {
            val newReplicaName = newReplica.name
            if (!currentRuntimeNames.contains(newReplicaName)) {
                val newRuntime = createReplicaRuntime(newReplica, this)
                replicaRuntimes[newReplicaName] = newRuntime
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
        }

    fun getReplicas(): List<TyeReplicaRuntime<TyeServiceReplica>> = replicaRuntimes.values.toList()

    override fun getVirtualFile(): VirtualFile? {
        return null
    }
}