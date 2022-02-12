package com.github.rafaelldi.tyeplugin.services

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service

@Service
class ProjectDisposable : Disposable {
    override fun dispose() {
    }
}