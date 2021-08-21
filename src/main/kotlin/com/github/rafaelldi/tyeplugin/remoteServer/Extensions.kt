package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceProjectRuntime
import com.intellij.remoteServer.util.CloudApplicationRuntime


fun TyeService.toRuntime(): CloudApplicationRuntime = when (this) {
    is TyeContainerService -> TODO()
    is TyeProjectService -> TyeServiceProjectRuntime(this)
    is TyeExecutableService -> TODO()
    is TyeExternalService -> TODO()
    is TyeFunctionService -> TODO()
    is TyeIngressService -> TODO()
}