package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeContainerService

class TyeServiceDockerRuntime(service: TyeContainerService, parentRuntime: TyeApplicationRuntime) :
    TyeServiceRuntime<TyeContainerService>(service, parentRuntime)