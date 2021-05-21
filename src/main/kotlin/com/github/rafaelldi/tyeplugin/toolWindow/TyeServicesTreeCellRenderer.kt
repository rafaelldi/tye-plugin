package com.github.rafaelldi.tyeplugin.toolWindow

import com.intellij.ui.ColoredTreeCellRenderer
import javax.swing.JTree

class TyeServicesTreeCellRenderer : ColoredTreeCellRenderer() {
    override fun customizeCellRenderer(
        tree: JTree,
        value: Any?,
        selected: Boolean,
        expanded: Boolean,
        leaf: Boolean,
        row: Int,
        hasFocus: Boolean
    ) {
        if (value is TyeServiceTreeNode){
            value.render(this)
        }
    }
}