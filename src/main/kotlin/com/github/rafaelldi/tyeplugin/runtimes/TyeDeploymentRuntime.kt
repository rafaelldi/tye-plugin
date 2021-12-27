package com.github.rafaelldi.tyeplugin.runtimes

import com.intellij.execution.process.KillableProcessHandler
import com.intellij.remoteServer.ServerType
import com.intellij.remoteServer.runtime.ServerTaskExecutor
import com.intellij.remoteServer.runtime.deployment.DeploymentRuntime
import com.intellij.remoteServer.util.AgentTaskExecutor
import com.intellij.remoteServer.util.CloudApplicationRuntime

class TyeDeploymentRuntime(private val handler: KillableProcessHandler) : CloudApplicationRuntime("default") {
    override fun undeploy(callback: UndeploymentTaskCallback) {
        handler.killProcess()
    }

    override fun getTaskExecutor(): ServerTaskExecutor {
        TODO("Not yet implemented")
    }

    override fun getAgentTaskExecutor(): AgentTaskExecutor {
        TODO("Not yet implemented")
    }

    override fun getCloudType(): ServerType<*> {
        TODO("Not yet implemented")
    }
}