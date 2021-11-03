package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager

fun TyeApplication.toRuntime(applicationManager: TyeApplicationManager): TyeApplicationRuntime =
    TyeApplicationRuntime(this, applicationManager)

fun TyeService.toRuntime(application: TyeApplicationRuntime): TyeServiceRuntime<*> = when (this) {
    is TyeContainerService -> TyeServiceDockerRuntime(this, application)
    is TyeProjectService -> TyeServiceProjectRuntime(this, application)
    is TyeExecutableService -> TyeServiceExecutableRuntime(this, application)
}

fun TyeServiceReplica.toRuntime(service: TyeServiceRuntime<*>): TyeReplicaRuntime<*> = when (this) {
    is TyeContainerServiceReplica -> TyeReplicaRuntime(this, service)
    is TyeProjectServiceReplica -> TyeReplicaRuntime(this, service)
}