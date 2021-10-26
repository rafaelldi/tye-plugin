package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.remoteServer.TyeHostType
import com.github.rafaelldi.tyeplugin.remoteServer.TyeServiceViewContributor
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.impl.runtime.ui.RemoteServersServiceViewContributor

class AddTyeHostAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        RemoteServersServiceViewContributor.addNewRemoteServer(project,
                TyeHostType.getInstance(),
                TyeServiceViewContributor::class.java)
    }

    override fun update(e: AnActionEvent) {
        e.presentation.text = "Tye Host"
    }
}