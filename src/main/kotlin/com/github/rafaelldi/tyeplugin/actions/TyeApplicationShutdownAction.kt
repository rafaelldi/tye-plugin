@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.util.ApplicationActionUtils
import kotlinx.coroutines.runBlocking

class TyeApplicationShutdownAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java) ?: return
        val tyeApplicationManager: TyeApplicationManager = service()
        runBlocking {
            tyeApplicationManager.shutdownApplication(runtime.host)
        }
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java)
        e.presentation.isEnabledAndVisible = runtime != null
    }
}