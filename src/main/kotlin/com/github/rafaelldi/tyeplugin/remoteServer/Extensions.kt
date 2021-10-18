package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.runtimes.*

fun TyeApplication.toRuntime(): TyeApplicationRuntime = TyeApplicationRuntime(this)

fun TyeService.toRuntime(): TyeBaseRuntime = when (this) {
    is TyeContainerService -> TyeServiceDockerRuntime(this)
    is TyeProjectService -> TyeServiceProjectRuntime(this)
    is TyeExecutableService -> TyeServiceExecutableRuntime(this)
    is TyeExternalService -> TyeServiceExternalRuntime(this)
    is TyeFunctionService -> TyeServiceFunctionRuntime(this)
    is TyeIngressService -> TyeServiceIngressRuntime(this)
}