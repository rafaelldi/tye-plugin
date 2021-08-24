package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.remoteServer.TyeHostType
import com.intellij.remoteServer.ServerType
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.util.AgentTaskExecutor
import com.intellij.remoteServer.util.CloudApplicationRuntime

sealed class TyeBaseRuntime(applicationName: String?) : CloudApplicationRuntime(applicationName) {
    override fun getCloudType(): ServerType<*> = TyeHostType.getInstance()

    override fun undeploy(callback: UndeploymentTaskCallback) {
        throw UnsupportedOperationException()
    }

    override fun getTaskExecutor(): ServerTaskExecutor {
        throw UnsupportedOperationException()
    }

    override fun getAgentTaskExecutor(): AgentTaskExecutor {
        throw UnsupportedOperationException()
    }
}