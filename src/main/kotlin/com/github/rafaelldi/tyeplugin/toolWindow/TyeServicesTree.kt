package com.github.rafaelldi.tyeplugin.toolWindow

import com.github.rafaelldi.tyeplugin.model.Service
import com.intellij.ui.treeStructure.Tree
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.SwingUtilities
import javax.swing.tree.TreeModel
import javax.swing.tree.TreeSelectionModel

class TyeServicesTree(treeModel: TreeModel) : Tree(treeModel) {
    init {
        isRootVisible = false
        emptyText.text = "No services to display."
        selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        setCellRenderer(TyeServicesTreeCellRenderer())
    }

    val selectedService: Service? get() {
        val path = selectionPath ?: return null
        val treeNode = path.getPathComponent(1) as? TyeServiceTreeNode ?: return null
        return treeNode.service
    }
}
