@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.runtimes.TyeReplicaRuntime
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.util.ApplicationActionUtils

class BrowseTyeReplicaAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeReplicaRuntime::class.java) ?: return
        val replicaUrl = runtime.getReplicaUrl() ?: return
        BrowserUtil.browse(replicaUrl)
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeReplicaRuntime::class.java)
        e.presentation.isEnabledAndVisible = runtime?.getReplicaUrl() != null
    }
}