package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.remoteServer.components.TyeDeploymentNodeComponent
import com.github.rafaelldi.tyeplugin.runtimes.*
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure.DeploymentNodeImpl
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection
import icons.TyeIcons
import javax.swing.JComponent

class TyeDeploymentNode(
    project: Project?,
    connection: ServerConnection<*>,
    serverNode: ServersTreeStructure.RemoteServerNode,
    value: Deployment,
    nodeProducer: ServersTreeStructure.DeploymentNodeProducer,
    nodeComponentProvider: TyeDeploymentNodeComponentProvider
) : DeploymentNodeImpl(project, connection, serverNode, value, nodeProducer) {
    private val nodeComponent: TyeDeploymentNodeComponent = nodeComponentProvider.getComponent(value)

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> {
        val result = ArrayList<AbstractTreeNode<Any>>()
        collectDeploymentChildren(result as List<AbstractTreeNode<*>>?)
        return result
    }

    override fun update(presentation: PresentationData) {
        super.update(presentation)
        val deployment = this.deployment
        when (deployment.runtime) {
            is TyeApplicationRuntime -> presentation.setIcon(TyeIcons.TYE)
            is TyeServiceProjectRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_DOT_NET)
            is TyeServiceDockerRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_DOCKER)
            is TyeServiceExecutableRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_EXECUTABLE)
            is TyeReplicaRuntime<*> -> presentation.setIcon(TyeIcons.TYE_NODE_REPLICA)
            else -> presentation.setIcon(TyeIcons.TYE)
        }
    }

    override fun getComponent(): JComponent = nodeComponent.getComponent()
}