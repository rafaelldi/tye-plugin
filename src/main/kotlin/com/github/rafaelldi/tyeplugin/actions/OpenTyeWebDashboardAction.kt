@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.remoteServer.TyeHostConfiguration
import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.intellij.execution.services.ServiceViewActionUtils
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.impl.runtime.ui.tree.DeploymentNode
import com.intellij.remoteServer.util.ApplicationActionUtils

class OpenTyeWebDashboardAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val node = ServiceViewActionUtils.getTarget(e, DeploymentNode::class.java) ?: return
        val deployment = ApplicationActionUtils.getDeployment(node) ?: return
        val config = deployment.connection.server.configuration as TyeHostConfiguration
        BrowserUtil.browse(config.hostAddress)
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeApplicationRuntime::class.java)
        e.presentation.isEnabledAndVisible = runtime != null
    }
}