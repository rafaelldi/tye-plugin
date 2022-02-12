package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ApplicationDeploymentNodeComponent(private val project: Project, private val runtime: TyeApplicationRuntime) :
    TyeDeploymentNodeComponent {
    private val panel: JPanel
    private val consoleTab: ConsoleTab

    init {
        panel = JPanel().apply {
            layout = BorderLayout()

            consoleTab = ConsoleTab(project)
            val tabbedPane = JBTabbedPane().apply {
                addTab(ConsoleTab.TITLE, consoleTab.component)
            }
            add(tabbedPane)
        }
    }

    override fun getComponent(): JComponent = panel

    override fun update() {
        val handler = runtime.getProcessHandler()
        if (handler != null) {
            consoleTab.attachToHandler(handler)
        }
    }
}