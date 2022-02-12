package com.github.rafaelldi.tyeplugin.remoteServer.deployment

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
    private val nodeComponentProvider: TyeDeploymentNodeComponentProvider
) : DeploymentNodeImpl(project, connection, serverNode, value, nodeProducer) {
    private var nodeComponent: TyeDeploymentNodeComponent? = null

    override fun getChildren(): MutableCollection<out AbstractTreeNode<*>> {
        val result = ArrayList<AbstractTreeNode<Any>>()
        collectDeploymentChildren(result as List<AbstractTreeNode<*>>?)
        return result
    }

    override fun update(presentation: PresentationData) {
        super.update(presentation)

        when (deployment.runtime) {
            is TyeApplicationRuntime -> presentation.setIcon(TyeIcons.TYE)
            is TyeServiceProjectRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_DOT_NET)
            is TyeServiceDockerRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_DOCKER)
            is TyeServiceExecutableRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_EXECUTABLE)
            is TyeServiceIngressRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_INGRESS)
            is TyeReplicaRuntime<*> -> presentation.setIcon(TyeIcons.TYE_NODE_REPLICA)
            else -> presentation.setIcon(TyeIcons.TYE)
        }
    }

    override fun getComponent(): JComponent? {
        if (nodeComponent == null && deployment.runtime != null) {
            nodeComponent = nodeComponentProvider.getComponent(deployment)
        }

        return nodeComponent?.getComponent()
    }
}