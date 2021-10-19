package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeApplication
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager

class TyeApplicationRuntime(application: TyeApplication, private val applicationManager: TyeApplicationManager) :
    TyeBaseRuntime("Tye Application: ${application.name}") {
    fun shutdownApplication() {
        applicationManager.shutdownApplication()
    }
}