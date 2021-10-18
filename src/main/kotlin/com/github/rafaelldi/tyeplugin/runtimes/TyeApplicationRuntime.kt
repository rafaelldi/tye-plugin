package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeApplication

class TyeApplicationRuntime(application: TyeApplication) : TyeBaseRuntime("Tye Application: ${application.name}")