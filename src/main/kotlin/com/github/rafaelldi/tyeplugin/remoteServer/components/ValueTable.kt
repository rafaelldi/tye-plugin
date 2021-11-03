package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.intellij.ui.table.JBTable
import javax.swing.table.TableModel

class ValueTable(model: TableModel) : JBTable(model) {
    override fun isCellEditable(row: Int, column: Int): Boolean = false
}