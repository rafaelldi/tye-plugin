package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.TyeApplication
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class TyeApplicationRuntime(private val application: TyeApplication, private val applicationManager: TyeApplicationManager) :
    TyeBaseRuntime("Tye Application: ${application.name}") {
    fun shutdownApplication() {
        applicationManager.shutdownApplication()
    }

    override fun getVirtualFile(): VirtualFile? {
        val path = application.source ?: return null
        return LocalFileSystem.getInstance().findFileByPath(path)
    }
}