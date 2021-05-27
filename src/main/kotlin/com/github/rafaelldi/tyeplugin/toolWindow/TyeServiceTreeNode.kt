package com.github.rafaelldi.tyeplugin.toolWindow

import com.github.rafaelldi.tyeplugin.model.Service
import com.intellij.ui.ColoredTreeCellRenderer
import icons.TyeIcons.TYE_NODE
import icons.TyeIcons.TYE_NODE_DOCKER
import icons.TyeIcons.TYE_NODE_DOT_NET
import javax.swing.tree.DefaultMutableTreeNode

sealed class TyeServiceTreeNode(val name: String?, val service: Service?) : DefaultMutableTreeNode() {
    companion object Factory {
        fun create(service: Service): TyeServiceTreeNode {
            return when (service) {
                is Service.External -> External(service.properties.name, service)
                is Service.Project -> Project(service.properties.name, service)
                is Service.Executable -> Executable(service.properties.name, service)
                is Service.Container -> Container(service.properties.name, service)
                is Service.Function -> Function(service.properties.name, service)
                is Service.Ingress -> Ingress(service.properties.name, service)
            }
        }
    }

    abstract fun render(renderer: ColoredTreeCellRenderer)

    class Root : TyeServiceTreeNode("Root", null) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Root")
        }
    }

    class External(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE
        }
    }

    class Project(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE_DOT_NET
        }
    }

    class Executable(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE
        }
    }

    class Container(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE_DOCKER
        }
    }

    class Function(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE
        }
    }

    class Ingress(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("$name")
            renderer.icon = TYE_NODE
        }
    }
}
