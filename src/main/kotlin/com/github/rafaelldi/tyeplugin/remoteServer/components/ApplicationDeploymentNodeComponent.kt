package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.github.rafaelldi.tyeplugin.remoteServer.components.tabs.ConsoleTab
import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.github.rafaelldi.tyeplugin.services.ProjectDisposable
import com.intellij.openapi.Disposable
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.components.JBTabbedPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ApplicationDeploymentNodeComponent(private val project: Project, private val runtime: TyeApplicationRuntime) :
    TyeDeploymentNodeComponent, Disposable {
    private val panel: JPanel
    private val consoleTab: ConsoleTab?

    init {
        panel = JPanel().apply {
            layout = BorderLayout()

            if (!runtime.isExternal) {
                consoleTab = ConsoleTab(project)
                val tabbedPane = JBTabbedPane().apply {
                    addTab(ConsoleTab.TITLE, consoleTab.component)
                }
                add(tabbedPane)
            } else {
                consoleTab = null
            }
        }

        if (consoleTab != null) {
            Disposer.register(this, consoleTab)
        }
        Disposer.register(project.service<ProjectDisposable>(), this)
    }

    override fun getComponent(): JComponent = panel

    override fun update() {
        runtime.getProcessHandler()?.let {
            consoleTab?.attachToHandler(it)
        }
    }

    override fun dispose() {
    }
}