package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.ide.util.treeView.AbstractTreeNode
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor.RemoteServerNodeDescriptor

class TyeNodeServiceViewDescriptor(
    node: AbstractTreeNode<*>,
    actionGroups: RemoteServersServiceViewContributor.ActionGroups
) : RemoteServerNodeDescriptor(node, actionGroups)