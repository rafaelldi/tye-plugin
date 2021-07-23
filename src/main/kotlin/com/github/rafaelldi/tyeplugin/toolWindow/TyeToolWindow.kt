package com.github.rafaelldi.tyeplugin.toolWindow

import com.github.rafaelldi.tyeplugin.messaging.TyeApplicationNotifier
import com.github.rafaelldi.tyeplugin.messaging.TyeApplicationNotifier.Companion.TOPIC
import com.github.rafaelldi.tyeplugin.model.Service
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.github.rafaelldi.tyeplugin.toolWindow.TyeServiceTreeNode.Factory.create
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
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
    private lateinit var propertiesTableModel: DefaultTableModel
    private lateinit var portBindingsTableModel: DefaultTableModel
    private lateinit var environmentVariablesTableModel: DefaultTableModel

    private var tyeApplicationManager: TyeApplicationManager = project.service()

    init {
        with(project.messageBus.connect()) {
            subscribe(
                TOPIC,
                object : TyeApplicationNotifier {
                    override fun connectedToTyeHost() {
                        val services = tyeApplicationManager.getServices()
                        updateTree(services)
                    }

                    override fun tyeApplicationUpdated() {
                        val services = tyeApplicationManager.getServices()
                        updateTree(services)
                    }

                    override fun tyeApplicationStopped() {
                        clearTable()
                        clearTree()
                    }
                }
            )
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
                    updateTable(tyeService)
                }
            })
            val treeScrollPane = JBScrollPane(tree)

            propertiesTableModel = DefaultTableModel(arrayOf("Name", "Value"), 0)
            portBindingsTableModel =
                DefaultTableModel(arrayOf("Name", "Protocol", "Host", "Port", "Container port", "Connection string"), 0)
            environmentVariablesTableModel = DefaultTableModel(arrayOf("Name", "Value"), 0)
            val tabbedPane = JBTabbedPane().apply {
                addTab("Properties", JBScrollPane(JBTable(propertiesTableModel)))
                addTab("Port Bindings", JBScrollPane(JBTable(portBindingsTableModel)))
                addTab("Environment Variables", JBScrollPane(JBTable(environmentVariablesTableModel)))
            }

            val splitter = JBSplitter(false, SPLITTER_PROPORTION).apply {
                firstComponent = treeScrollPane
                secondComponent = tabbedPane
            }
            add(splitter)
        }
    }

    private fun initActionToolbar() {
        val actionManager = ActionManager.getInstance()
        val actionGroup =
            actionManager.getAction("com.github.rafaelldi.tyeplugin.actions.TyeToolWindowGroupedActions") as ActionGroup
        val actionToolbar = actionManager.createActionToolbar("TyeActionToolbar", actionGroup, true)
        toolbar = actionToolbar.component
    }

    private fun root(): DefaultMutableTreeNode = treeModel.root as DefaultMutableTreeNode

    private fun updateTree(services: List<Service>) {
        val root = root()
        root.removeAllChildren()

        for (service in services) {
            val serviceNode: DefaultMutableTreeNode = create(service)
            treeModel.insertNodeInto(serviceNode, root, 0)
            root.add(serviceNode)
        }

        treeModel.nodeStructureChanged(root)
    }

    private fun clearTree() {
        val root = root()
        root.removeAllChildren()
        treeModel.nodeStructureChanged(root)
    }

    private fun updateTable(service: Service?) {
        if (service == null) return

        with(propertiesTableModel) {
            dataVector.removeAllElements()
            addRow(arrayOf("Id", service.properties.id))
            addRow(arrayOf("Type", service.properties.type))
            addRow(arrayOf("Replicas", service.properties.replicas))
            addRow(arrayOf("Restarts", service.properties.restarts))
            addRow(arrayOf("Project", service.properties.project))
            addRow(arrayOf("Image", service.properties.image))
            addRow(arrayOf("Executable", service.properties.executable))
            addRow(arrayOf("Working directory", service.properties.workingDirectory))
            fireTableStructureChanged()
        }

        with(portBindingsTableModel) {
            dataVector.removeAllElements()
            for (bind in service.bindings) {
                addRow(
                    arrayOf(
                        bind.name,
                        bind.protocol,
                        bind.host,
                        bind.port,
                        bind.containerPort,
                        bind.connectionString
                    )
                )
            }
            fireTableStructureChanged()
        }

        with(environmentVariablesTableModel) {
            dataVector.removeAllElements()
            for (variable in service.environmentVariables) {
                addRow(arrayOf(variable.name, variable.value))
            }
            fireTableStructureChanged()
        }
    }

    private fun clearTable() {
        with(propertiesTableModel) {
            dataVector.removeAllElements()
            fireTableStructureChanged()
        }

        with(portBindingsTableModel) {
            dataVector.removeAllElements()
            fireTableStructureChanged()
        }

        with(environmentVariablesTableModel) {
            dataVector.removeAllElements()
            fireTableStructureChanged()
        }
    }

    companion object {
        const val SPLITTER_PROPORTION = 0.2F
    }
}
