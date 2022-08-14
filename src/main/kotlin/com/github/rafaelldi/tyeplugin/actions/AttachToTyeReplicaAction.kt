@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.runtimes.TyeReplicaRuntime
import com.intellij.execution.process.impl.ProcessListUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.remoteServer.util.ApplicationActionUtils
import com.intellij.xdebugger.attach.LocalAttachHost
import com.intellij.xdebugger.attach.XAttachDebuggerProvider

class AttachToTyeReplicaAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeReplicaRuntime::class.java) ?: return
        val pid = runtime.getReplicaPid()?.toInt() ?: return

        runBackgroundableTask("Attach debugger", e.project) {
            val processInfo = ProcessListUtil.getProcessList().firstOrNull { it.pid == pid }
                ?: return@runBackgroundableTask
            val attachHost = LocalAttachHost.INSTANCE
            val dataHolder = UserDataHolderBase()
            val debugger = XAttachDebuggerProvider.EP.extensionList
                .filter { it.isAttachHostApplicable(attachHost) }
                .flatMap { it.getAvailableDebuggers(project, attachHost, processInfo, dataHolder) }
                .singleOrNull { it.debuggerDisplayName == ".NET Core Debugger" }
                ?: return@runBackgroundableTask

            debugger.attachDebugSession(project, attachHost, processInfo)
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeReplicaRuntime::class.java)
        e.presentation.isEnabledAndVisible = project != null && runtime?.getReplicaPid() != null
    }
}