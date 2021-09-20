package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceDockerRuntime
import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceExecutableRuntime
import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceProjectRuntime
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
        val deployment = this.deployment
        when (deployment.runtime) {
            is TyeServiceProjectRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_DOT_NET)
            is TyeServiceDockerRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_DOCKER)
            is TyeServiceExecutableRuntime -> presentation.setIcon(TyeIcons.TYE_NODE_EXECUTABLE)
            else -> presentation.setIcon(TyeIcons.TYE_NODE)
        }
    }

    override fun getComponent(): JComponent {
        if (nodeComponent == null) {
            nodeComponent = nodeComponentProvider.getComponent(value)
        }

        return nodeComponent!!.getComponent()
    }
}