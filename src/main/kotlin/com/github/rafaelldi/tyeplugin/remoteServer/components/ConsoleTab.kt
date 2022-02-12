package com.github.rafaelldi.tyeplugin.remoteServer.components

import com.intellij.execution.filters.TextConsoleBuilderFactory
import com.intellij.execution.process.ColoredProcessHandler
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.project.Project
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

class ConsoleTab(project: Project) {
    companion object {
        const val TITLE = "Console"
    }

    val component: JComponent

    private var activeProcessHandler: KillableProcessHandler? = null
    private val consoleView: ConsoleView = TextConsoleBuilderFactory.getInstance()
        .createBuilder(project).apply { setViewer(true) }.console

    init {
        component = JPanel().apply {
            layout = BorderLayout()

            add(consoleView.component, BorderLayout.CENTER)
        }
    }

    fun attachToHandler(processHandler: ColoredProcessHandler) {
        if (activeProcessHandler != processHandler) {
            activeProcessHandler?.detachProcess()
            activeProcessHandler = processHandler
            consoleView.attachToProcess(processHandler)
        }
    }
}