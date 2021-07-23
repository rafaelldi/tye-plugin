package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.execution.services.ServiceViewDescriptor
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.configuration.RemoteServer
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor
import com.intellij.remoteServer.impl.runtime.ui.tree.ServersTreeStructure
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection

class TyeServiceViewContributor : RemoteServersServiceViewContributor() {
    override fun getViewDescriptor(project: Project): ServiceViewDescriptor {
        TODO("Not yet implemented")
    }

    override fun createDeploymentNode(
        connection: ServerConnection<*>?,
        serverNode: ServersTreeStructure.RemoteServerNode?,
        deployment: Deployment?
    ): AbstractTreeNode<*> {
        TODO("Not yet implemented")
    }

    override fun accept(server: RemoteServer<*>): Boolean {
        TODO("Not yet implemented")
    }

    override fun selectLog(deploymentNode: AbstractTreeNode<*>, logName: String) {
        TODO("Not yet implemented")
    }

    override fun getActionGroups(): ActionGroups {
        TODO("Not yet implemented")
    }
}