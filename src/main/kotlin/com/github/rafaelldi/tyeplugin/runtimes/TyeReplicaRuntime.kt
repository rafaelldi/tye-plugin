package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica
import com.intellij.openapi.vfs.VirtualFile

class TyeReplicaRuntime<T>(val replica: T, parentRuntime: TyeServiceRuntime<*>) :
    TyeBaseRuntime(replica.name) where T : TyeServiceReplica {
    init {
        parent = parentRuntime
    }

    override fun getSourceFile(): VirtualFile? {
        return parent?.getSourceFile()
    }
}