package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica
import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica.Companion.REPLICA_PROPERTY_PID_KEY
import com.intellij.openapi.vfs.VirtualFile

class TyeReplicaRuntime<T>(val replica: T, parentRuntime: TyeServiceRuntime<*>) :
    TyeBaseRuntime(replica.getName()) where T : TyeServiceReplica {
    init {
        parent = parentRuntime
    }

    private var url: String? = null

    fun update(updatedReplica: T) {
        updateProperties(updatedReplica.properties)
        updateEnvironmentVariables(updatedReplica.environmentVariables)
        updateUrl()
    }

    private fun updateProperties(updatedProperties: MutableMap<String, String?>) {
        val removedProperties = replica.properties.keys.subtract(updatedProperties.keys)

        for (property in updatedProperties) {
            val currentValue = replica.properties[property.key]
            if (currentValue == property.value) {
                continue
            }

            replica.properties[property.key] = property.value
        }

        for (removedProperty in removedProperties) {
            replica.properties.remove(removedProperty)
        }
    }

    private fun updateEnvironmentVariables(updatedVariables: MutableMap<String, String?>?) {
        if (updatedVariables == null || replica.environmentVariables == null) {
            return
        }

        val removedVariables = replica.environmentVariables.keys.subtract(updatedVariables.keys)

        for (variable in updatedVariables) {
            val currentValue = replica.environmentVariables[variable.key]
            if (currentValue == variable.value) {
                continue
            }

            replica.environmentVariables[variable.key] = variable.value
        }

        for (removedVariable in removedVariables) {
            replica.environmentVariables.remove(removedVariable)
        }
    }

    private fun updateUrl() {
        val parentName = parent?.applicationName?.uppercase() ?: return
        if (replica.environmentVariables == null) {
            return
        }

        val protocolVariable = replica.environmentVariables["SERVICE__${parentName}__PROTOCOL"] ?: return
        val hostVariable = replica.environmentVariables["SERVICE__${parentName}__HOST"] ?: return
        val portVariable = replica.environmentVariables["SERVICE__${parentName}__PORT"] ?: return

        url = "${protocolVariable}://${hostVariable}:${portVariable}"
    }

    override fun getSourceFile(): VirtualFile? {
        return parent?.getSourceFile()
    }

    fun getReplicaUrl(): String? = url

    fun getReplicaPid(): String? = replica.properties[REPLICA_PROPERTY_PID_KEY]
}