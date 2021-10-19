package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeExecutableService

class TyeServiceExecutableRuntime(service: TyeExecutableService, parentRuntime: TyeApplicationRuntime) :
    TyeServiceRuntime<TyeExecutableService>(service, parentRuntime)