package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeExternalService

class TyeServiceExternalRuntime(service: TyeExternalService, parentRuntime: TyeApplicationRuntime) :
    TyeServiceRuntime<TyeExternalService>(service, parentRuntime)