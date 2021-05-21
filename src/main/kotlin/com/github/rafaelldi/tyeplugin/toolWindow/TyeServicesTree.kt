package com.github.rafaelldi.tyeplugin.toolWindow

import com.intellij.ui.treeStructure.Tree
import javax.swing.tree.TreeModel
import javax.swing.tree.TreeSelectionModel

class TyeServicesTree(treeModel: TreeModel) : Tree(treeModel) {
    init {
        isRootVisible = false
        emptyText.text = "There are no tye services to display."
        selectionModel.selectionMode = TreeSelectionModel.SINGLE_TREE_SELECTION
        setCellRenderer(TyeServicesTreeCellRenderer())
    }
}