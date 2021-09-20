package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.runtimes.*
import com.intellij.remoteServer.util.CloudApplicationRuntime


fun TyeService.toRuntime(): CloudApplicationRuntime = when (this) {
    is TyeContainerService -> TyeServiceDockerRuntime(this)
    is TyeProjectService -> TyeServiceProjectRuntime(this)
    is TyeExecutableService -> TyeServiceExecutableRuntime(this)
    is TyeExternalService -> TyeServiceExternalRuntime(this)
    is TyeFunctionService -> TyeServiceFunctionRuntime(this)
    is TyeIngressService -> TyeServiceIngressRuntime(this)
}