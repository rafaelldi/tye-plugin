@file:Suppress("UnstableApiUsage")

package com.github.rafaelldi.tyeplugin.actions

import com.github.rafaelldi.tyeplugin.runtimes.TyeServiceRuntime
import com.intellij.ide.BrowserUtil
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.remoteServer.util.ApplicationActionUtils

class BrowseTyeServiceAction : DumbAwareAction(){
    override fun actionPerformed(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeServiceRuntime::class.java) ?: return
        val serviceUrl = runtime.getServiceUrl() ?: return
        BrowserUtil.browse(serviceUrl)
    }

    override fun update(e: AnActionEvent) {
        val runtime = ApplicationActionUtils.getApplicationRuntime(e, TyeServiceRuntime::class.java)
        e.presentation.isEnabledAndVisible = runtime?.getServiceUrl() != null
    }
}