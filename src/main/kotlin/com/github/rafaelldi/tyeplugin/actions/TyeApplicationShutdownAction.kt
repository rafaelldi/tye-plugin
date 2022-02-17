package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.util.ApplicationActionUtils

class TyeApplicationShutdownAction : DumbAwareAction() {
    private var tyeApplicationManager: TyeApplicationManager = service()

    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java) ?: return

        runBackgroundableTask("Tye shutdown", e.project) {
            it.isIndeterminate = true
            it.text = "Tye application is stopping"
            tyeApplicationManager.shutdownApplication(runtime.host)
        }
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java)
        e.presentation.isEnabledAndVisible = runtime != null
    }
}