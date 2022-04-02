package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeProjectService
import com.github.rafaelldi.tyeplugin.model.TyeService.Companion.PROPERTY_PROJECT_KEY
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class TyeServiceProjectRuntime(service: TyeProjectService, parentRuntime: TyeApplicationRuntime) :
    TyeServiceRuntime<TyeProjectService>(service, parentRuntime) {
    override fun getSourceFile(): VirtualFile? {
        val path = service.properties[PROPERTY_PROJECT_KEY] ?: return null
        return LocalFileSystem.getInstance().findFileByPath(path)
    }
}