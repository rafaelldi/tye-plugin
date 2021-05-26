package com.github.rafaelldi.tyeplugin.toolWindow

import com.github.rafaelldi.tyeplugin.model.Service
import com.intellij.openapi.externalSystem.service.execution.NotSupportedException
import com.intellij.ui.ColoredTreeCellRenderer
import icons.TyeIcons.TYE_NODE
import icons.TyeIcons.TYE_NODE_DOCKER
import icons.TyeIcons.TYE_NODE_DOT_NET
import javax.swing.tree.DefaultMutableTreeNode

abstract class TyeServiceTreeNode(val name: String?, val service: Service?) : DefaultMutableTreeNode() {
    companion object Factory {
        fun create(service: Service): TyeServiceTreeNode {
            return when (service) {
                is Service.External -> External(service.name, service)
                is Service.Project -> Project(service.name, service)
                is Service.Executable -> Executable(service.name, service)
                is Service.Container -> Container(service.name, service)
                is Service.Function -> Function(service.name, service)
                is Service.Ingress -> Ingress(service.name, service)
                else -> throw NotSupportedException("$service is not supported")
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
            renderer.append("External $name")
            renderer.icon = TYE_NODE
        }
    }

    class Project(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Project $name")
            renderer.icon = TYE_NODE_DOT_NET
        }
    }

    class Executable(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Executable $name")
            renderer.icon = TYE_NODE
        }
    }

    class Container(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Container $name")
            renderer.icon = TYE_NODE_DOCKER
        }
    }

    class Function(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Function $name")
            renderer.icon = TYE_NODE
        }
    }

    class Ingress(name: String?, service: Service) : TyeServiceTreeNode(name, service) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Ingress $name")
            renderer.icon = TYE_NODE
        }
    }
}
