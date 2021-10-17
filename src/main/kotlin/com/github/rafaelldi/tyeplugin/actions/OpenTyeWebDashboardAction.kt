package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.remoteServer.TyeHostConfiguration
import com.github.rafaelldi.tyeplugin.remoteServer.TyeHostType
import com.intellij.execution.services.ServiceViewActionUtils
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeNode
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure.RemoteServerNode

class OpenTyeWebDashboardAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val node = ServiceViewActionUtils.getTarget(e, ServersTreeNode::class.java)
        if (node !is RemoteServerNode || node.server.type !is TyeHostType || !node.isConnected)
            return

        val config = node.server.configuration as TyeHostConfiguration
        BrowserUtil.browse(config.hostAddress)
    }

    override fun update(e: AnActionEvent) {
        val node = ServiceViewActionUtils.getTarget(e, ServersTreeNode::class.java)
        if (node is RemoteServerNode && node.server.type is TyeHostType) {
            e.presentation.isVisible = true
            e.presentation.isEnabled = node.isConnected
        } else {
            e.presentation.isEnabledAndVisible = false
        }
    }
}