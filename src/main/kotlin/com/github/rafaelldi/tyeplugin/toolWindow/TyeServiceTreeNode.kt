package com.github.rafaelldi.tyeplugin.toolWindow

import com.github.rafaelldi.tyeplugin.model.Service
import com.intellij.openapi.externalSystem.service.execution.NotSupportedException
import com.intellij.ui.ColoredTreeCellRenderer
import icons.TyeIcons.TYE_NODE_DOT_NET
import javax.swing.tree.DefaultMutableTreeNode


abstract class TyeServiceTreeNode(val name: String?) : DefaultMutableTreeNode() {
    companion object Factory {
        fun create(service: Service): TyeServiceTreeNode {
            return when(service) {
                is Service.External -> External(service.name)
                is Service.Project -> Project(service.name)
                is Service.Executable -> Executable(service.name)
                is Service.Container -> Container(service.name)
                is Service.Function -> Function(service.name)
                is Service.Ingress -> Ingress(service.name)
                else -> throw NotSupportedException("$service is not supported")
            }
        }
    }

    abstract fun render(renderer: ColoredTreeCellRenderer)

    class Root : TyeServiceTreeNode("Root") {
        override fun render(renderer: ColoredTreeCellRenderer) {
        }
    }

    class External(name: String?) : TyeServiceTreeNode(name) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("External $name")
        }
    }

    class Project(name: String?) : TyeServiceTreeNode(name) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Project $name")
            renderer.icon = TYE_NODE_DOT_NET
        }
    }

    class Executable(name: String?) : TyeServiceTreeNode(name) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Executable $name")
        }
    }

    class Container(name: String?) : TyeServiceTreeNode(name) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Container $name")
        }
    }

    class Function(name: String?) : TyeServiceTreeNode(name) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Function $name")
        }
    }

    class Ingress(name: String?) : TyeServiceTreeNode(name) {
        override fun render(renderer: ColoredTreeCellRenderer) {
            renderer.append("Ingress $name")
        }
    }
}