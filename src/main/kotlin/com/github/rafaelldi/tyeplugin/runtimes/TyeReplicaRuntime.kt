package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeServiceReplica

class TyeReplicaRuntime<T>(val replica: T, parentRuntime: TyeServiceRuntime<*>) :
    TyeBaseRuntime(replica.name) where T : TyeServiceReplica {
    init {
        parent = parentRuntime
    }
}