package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure.DeploymentNodeImpl
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection
import com.intellij.remoteServer.runtime.deployment.DeploymentStatus

class TyeDeploymentNode(
    project: Project?,
    connection: ServerConnection<*>,
    serverNode: ServersTreeStructure.RemoteServerNode,
    value: Deployment,
    nodeProducer: ServersTreeStructure.DeploymentNodeProducer
) : DeploymentNodeImpl(project, connection, serverNode, value, nodeProducer) {
    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> {
        val result = ArrayList<AbstractTreeNode<Any>>()
        collectDeploymentChildren(result as List<AbstractTreeNode<*>>?)
        return result
    }

    override fun update(presentation: PresentationData) {
        super.update(presentation)
        val deployment = this.deployment
        val runtime = deployment.runtime
        presentation.setIcon(AllIcons.General.Information)

    }
}