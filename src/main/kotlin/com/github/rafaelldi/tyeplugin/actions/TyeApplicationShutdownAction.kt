package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.intellij.execution.services.ServiceViewActionUtils
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.impl.runtime.ui.tree.DeploymentNode
import com.intellij.remoteServer.util.ApplicationActionUtils

class TyeApplicationShutdownAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val node = ServiceViewActionUtils.getTarget(e, DeploymentNode::class.java) ?: return
        val deployment = ApplicationActionUtils.getDeployment(node) ?: return
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java) ?: return

        runBackgroundableTask("Tye Shutdown", e.project) {
            it.isIndeterminate = true
            it.text = "Tye application is stopping"

            runtime.shutdownApplication()
        }

        deployment.connection.disconnect()
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java)
        e.presentation.isEnabledAndVisible = runtime != null
    }
}