package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeFunctionService

class TyeServiceFunctionRuntime(service: TyeFunctionService, parentRuntime: TyeApplicationRuntime) :
    TyeServiceRuntime<TyeFunctionService>(service, parentRuntime)