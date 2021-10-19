package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeProjectService

class TyeServiceProjectRuntime(service: TyeProjectService, parentRuntime: TyeApplicationRuntime) :
    TyeServiceRuntime<TyeProjectService>(service, parentRuntime)