package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.execution.services.ServiceViewDescriptor
import com.intellij.execution.services.ServiceViewLazyContributor
import com.intellij.execution.services.SimpleServiceViewDescriptor
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection
import icons.TyeIcons

class TyeServiceViewContributor : RemoteServersServiceViewContributor(), ServiceViewLazyContributor {
    companion object {
        private val serviceViewDescriptor = SimpleServiceViewDescriptor("Tye", TyeIcons.TYE)
        private val nodeComponentProvider: TyeDeploymentNodeComponentProvider = TyeDeploymentNodeComponentProvider()
    }

    override fun getViewDescriptor(project: Project): ServiceViewDescriptor = serviceViewDescriptor

    override fun createDeploymentNode(
        connection: ServerConnection<*>?,
        serverNode: ServersTreeStructure.RemoteServerNode?,
        deployment: Deployment?
    ): AbstractTreeNode<*> = TyeDeploymentNode(
        serverNode!!.project,
        connection!!,
        serverNode,
        deployment!!,
        this,
        nodeComponentProvider
    )

    override fun accept(server: RemoteServer<*>): Boolean = server.type == TyeHostType.getInstance()

    override fun selectLog(deploymentNode: AbstractTreeNode<*>, logName: String) {
        TODO("Not yet implemented")
    }

    override fun getActionGroups(): ActionGroups = ActionGroups(
        "Tye.RemoteServers.Main",
        "Tye.RemoteServers.Secondary",
        "Tye.RemoteServers.Toolbar"
    )

    override fun createNodeContributor(node: AbstractTreeNode<*>): RemoteServerNodeServiceViewContributor =
        TyeNodeServiceViewContributor(this, node)
}