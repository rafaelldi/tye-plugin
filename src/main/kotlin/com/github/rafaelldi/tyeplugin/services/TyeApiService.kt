package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.api.TyeApiClient
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Service
final class TyeApiService(private val project: Project) {
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
    private val client: TyeApiClient

    init {
        val host = "http://localhost:8000"
        client = TyeApiClient(host)
    }

    fun updateServices() {
        scope.launch {
            val services = client.getServices()
        }
    }
}