package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeService
import com.intellij.openapi.vfs.VirtualFile

sealed class TyeServiceRuntime<T>(val service: T, parentRuntime: TyeApplicationRuntime) :
    TyeBaseRuntime(service.getServiceName()) where T : TyeService {
    init {
        parent = parentRuntime
    }

    override fun getVirtualFile(): VirtualFile? = parent?.getVirtualFile()
}