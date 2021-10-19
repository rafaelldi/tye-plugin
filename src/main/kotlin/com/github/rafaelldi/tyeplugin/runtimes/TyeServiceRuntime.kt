package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeService

sealed class TyeServiceRuntime<T>(val service: T, parentRuntime: TyeApplicationRuntime) :
    TyeBaseRuntime(service.getServiceName()) where T : TyeService {
    init {
        parent = parentRuntime
    }
}