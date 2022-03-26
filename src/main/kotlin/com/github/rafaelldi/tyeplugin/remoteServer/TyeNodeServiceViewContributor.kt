@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.execution.services.ServiceViewDescriptor
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor.RemoteServerNodeServiceViewContributor

class TyeNodeServiceViewContributor(rootContributor: RemoteServersServiceViewContributor, node: AbstractTreeNode<*>) :
    RemoteServerNodeServiceViewContributor(rootContributor, node) {
    override fun getViewDescriptor(project: Project): ServiceViewDescriptor =
        TyeNodeServiceViewDescriptor(this.asService(), this.rootContributor.actionGroups)
}