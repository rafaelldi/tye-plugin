package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeProjectService
import com.github.rafaelldi.tyeplugin.model.TyeProjectServiceProperties
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class TyeServiceProjectRuntime(service: TyeProjectService, parentRuntime: TyeApplicationRuntime) :
    TyeServiceRuntime<TyeProjectService>(service, parentRuntime) {
    override fun getSourceFile(): VirtualFile? {
        val path = (service.properties as? TyeProjectServiceProperties)?.project ?: return null
        return LocalFileSystem.getInstance().findFileByPath(path)
    }
}