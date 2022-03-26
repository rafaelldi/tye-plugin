@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.remoteServer

import com.github.rafaelldi.tyeplugin.runtimes.TyeBaseRuntime
import com.intellij.execution.services.ServiceViewLocatableDescriptor
import com.intellij.ide.util.PsiNavigationSupport
import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.pom.Navigatable
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor.RemoteServerNodeDescriptor
import com.intellij.remoteServer.impl.runtime.ui.tree.DeploymentNode
import com.intellij.remoteServer.util.ApplicationActionUtils

class TyeNodeServiceViewDescriptor(
    node: AbstractTreeNode<*>,
    actionGroups: RemoteServersServiceViewContributor.ActionGroups
) : RemoteServerNodeDescriptor(node, actionGroups), ServiceViewLocatableDescriptor {
    override fun getNavigatable(): Navigatable? {
        if (node !is DeploymentNode) {
            return null
        }
        val project = node.project ?: return null
        val runtime = ApplicationActionUtils.getApplicationRuntime(node as DeploymentNode, TyeBaseRuntime::class.java)
        val file = runtime?.getSourceFile() ?: return null

        return PsiNavigationSupport.getInstance().createNavigatable(project, file, 0)
    }
}