@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.github.rafaelldi.tyeplugin.services.TyeCliService
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.components.service
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.util.ApplicationActionUtils

class TyePushAction: DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java) ?: return
        val sourcePath = runtime.getSourceFile()?.path ?: return
        val tyeCliService = project.service<TyeCliService>()

        runBackgroundableTask("Tye push", e.project) {
            it.isIndeterminate = true
            it.text = "Tye push command is running"
            tyeCliService.runPushCommand(sourcePath, project.basePath)
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java)
        val sourcePath = runtime?.getSourceFile()?.path
        e.presentation.isEnabledAndVisible = project != null && sourcePath != null
    }
}