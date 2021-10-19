package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeIngressService

class TyeServiceIngressRuntime(service: TyeIngressService, parentRuntime: TyeApplicationRuntime) :
    TyeServiceRuntime<TyeIngressService>(service, parentRuntime)