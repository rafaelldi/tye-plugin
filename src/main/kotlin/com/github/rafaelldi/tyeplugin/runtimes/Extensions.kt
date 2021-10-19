package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager

fun TyeApplication.toRuntime(applicationManager: TyeApplicationManager): TyeApplicationRuntime =
    TyeApplicationRuntime(this, applicationManager)

fun TyeService.toRuntime(application: TyeApplicationRuntime): TyeBaseRuntime = when (this) {
    is TyeContainerService -> TyeServiceDockerRuntime(this, application)
    is TyeProjectService -> TyeServiceProjectRuntime(this, application)
    is TyeExecutableService -> TyeServiceExecutableRuntime(this, application)
    is TyeExternalService -> TyeServiceExternalRuntime(this, application)
    is TyeFunctionService -> TyeServiceFunctionRuntime(this, application)
    is TyeIngressService -> TyeServiceIngressRuntime(this, application)
}