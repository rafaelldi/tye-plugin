package com.github.rafaelldi.tyeplugin.toolWindow

import com.github.rafaelldi.tyeplugin.model.*
import com.intellij.ui.ColoredTreeCellRenderer
import icons.TyeIcons.TYE_NODE
import icons.TyeIcons.TYE_NODE_DOCKER
import icons.TyeIcons.TYE_NODE_DOT_NET
import icons.TyeIcons.TYE_NODE_EXECUTABLE
import javax.swing.tree.DefaultMutableTreeNode

sealed class TyeServiceTreeNode(val name: String?, val service: TyeService?) : DefaultMutableTreeNode() {
    companion object Factory {
        fun create(service: TyeService): TyeServiceTreeNode {
            return when (service) {
                is TyeExternalService -> External(service.properties.id, service)
                is TyeProjectService -> Project(service.properties.id, service)
                is TyeExecutableService -> Executable(service.properties.id, service)
                is TyeContainerService -> Container(service.properties.id, service)
                is TyeFunctionService -> Function(service.properties.id, service)
                is TyeIngressService -> Ingress(service.properties.id, service)
            }
        }
    }

    abstract fun render(renderer: ColoredTreeCellRenderer)

    class Root : TyeServiceTreeNode("Root", null) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Root")
        }
    }

    class External(name: String?, service: TyeService) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE
        }
    }

    class Project(name: String?, service: TyeService) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE_DOT_NET
        }
    }

    class Executable(name: String?, service: TyeService) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE_EXECUTABLE
        }
    }

    class Container(name: String?, service: TyeService) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE_DOCKER
        }
    }

    class Function(name: String?, service: TyeService) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE
        }
    }

    class Ingress(name: String?, service: TyeService) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE
        }
    }
}
