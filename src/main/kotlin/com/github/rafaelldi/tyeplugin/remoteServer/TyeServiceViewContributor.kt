package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.execution.services.ServiceViewDescriptor
import com.intellij.execution.services.SimpleServiceViewDescriptor
import com.intellij.icons.AllIcons
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection

class TyeServiceViewContributor : RemoteServersServiceViewContributor() {
    override fun getViewDescriptor(project: Project): ServiceViewDescriptor {
        return SimpleServiceViewDescriptor("Tye", AllIcons.General.Balloon)
    }

    override fun createDeploymentNode(
        connection: ServerConnection<*>?,
        serverNode: ServersTreeStructure.RemoteServerNode?,
        deployment: Deployment?
    ): AbstractTreeNode<*> {
        return TyeDeploymentNode(serverNode!!.project, connection!!, serverNode, deployment!!, this)
    }

    override fun accept(server: RemoteServer<*>): Boolean {
        return server.type == TyeHostType.getInstance()
    }

    override fun selectLog(deploymentNode: AbstractTreeNode<*>, logName: String) {
        TODO("Not yet implemented")
    }

    override fun getActionGroups(): ActionGroups = ActionGroups(
        "Tye.RemoteServersViewToolbar.Main",
        "Tye.RemoteServersViewToolbar.Secondary",
        "Tye.RemoteServersViewToolbar.Toolbar"
    )

    override fun createNodeContributor(node: AbstractTreeNode<*>): RemoteServerNodeServiceViewContributor =
        TyeNodeServiceViewContributor(this, node)
}