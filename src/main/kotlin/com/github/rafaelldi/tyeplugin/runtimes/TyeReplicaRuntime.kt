package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica
import com.intellij.openapi.vfs.VirtualFile

class TyeReplicaRuntime<T>(val replica: T, parentRuntime: TyeServiceRuntime<*>) :
    TyeBaseRuntime(replica.getName()) where T : TyeServiceReplica {
    init {
        parent = parentRuntime
    }

    fun update(updatedReplica: T) {
        updateProperties(updatedReplica.properties)
        updateEnvironmentVariables(updatedReplica.environmentVariables)
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

    override fun getSourceFile(): VirtualFile? {
        return parent?.getSourceFile()
    }
}