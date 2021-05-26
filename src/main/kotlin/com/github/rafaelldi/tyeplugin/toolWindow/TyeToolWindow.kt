package com.github.rafaelldi.tyeplugin.toolWindow


import com.github.rafaelldi.tyeplugin.messaging.TyeServicesNotifier
import com.github.rafaelldi.tyeplugin.messaging.TyeServicesNotifier.Companion.TOPIC
import com.github.rafaelldi.tyeplugin.model.Service
import com.github.rafaelldi.tyeplugin.model.Tye
import com.github.rafaelldi.tyeplugin.services.TyeApiService
import com.github.rafaelldi.tyeplugin.toolWindow.TyeServiceTreeNode.Factory.create
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.table.JBTable
import java.awt.BorderLayout
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JPanel
import javax.swing.SwingUtilities
import javax.swing.table.DefaultTableModel
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class TyeToolWindow(project: Project) : SimpleToolWindowPanel(false) {
    private lateinit var panel: JPanel
    private lateinit var treeModel: DefaultTreeModel
    private lateinit var tree: TyeServicesTree
    private lateinit var tableModel: DefaultTableModel

    private var tyeApiService: TyeApiService = project.getService(TyeApiService::class.java)

    init {
        with(project.messageBus.connect()) {
            subscribe(TOPIC, object : TyeServicesNotifier {
                override fun tyeServicesUpdated() {
                    val tye = tyeApiService.getTye()
                    updateTree(tye)
                }
            })
        }

        createUIComponents()
        initActionToolbar()
        setContent(panel)
    }

    private fun createUIComponents() {
        panel = JPanel().apply {
            layout = BorderLayout()

            treeModel = DefaultTreeModel(TyeServiceTreeNode.Root())
            tree = TyeServicesTree(treeModel)
            tree.addMouseListener(object : MouseAdapter() {
                override fun mouseClicked(e: MouseEvent) {
                    if (!SwingUtilities.isLeftMouseButton(e)) return
                    val tree = e.source as? TyeServicesTree ?: return
                    val tyeService = tree.selectedService
                    selectTyeService(tyeService)
                }
            })

            val treeScrollPane = JBScrollPane(tree)

            tableModel = DefaultTableModel(arrayOf("Name", "Value"), 0)
            val table = JBTable(tableModel)
            table.setShowColumns(true)

            val tableScrollPane = JBScrollPane(table)

            val splitter = JBSplitter(false, 0.3F).apply {
                firstComponent = treeScrollPane
                secondComponent = tableScrollPane
            }
            add(splitter)
        }
    }

    private fun initActionToolbar(){
        val actionManager = ActionManager.getInstance()
        val actionGroup =
            actionManager.getAction("com.github.rafaelldi.tyeplugin.actions.TyeToolWindowGroupedActions") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar("TyeActionToolbar", actionGroup, true)
        toolbar = actionToolbar.component
    }

    private fun root(): DefaultMutableTreeNode = treeModel.root as DefaultMutableTreeNode

    private fun updateTree(tye: Tye) {
        val root = root()
        root.removeAllChildren()

        for (service in tye.getServices()) {
            val serviceNode: DefaultMutableTreeNode = create(service)
            treeModel.insertNodeInto(serviceNode, root, 0)
            root.add(serviceNode)
        }

        treeModel.nodeStructureChanged(root)
    }

    private fun selectTyeService(service: Service?){
        if (service == null)
            return
    }
}