package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.remoteServer.runtime.ServerConnector
import com.intellij.remoteServer.runtime.ServerTaskExecutor

class TyeConnector(private val configuration: TyeHostConfiguration, private val taskExecutor: ServerTaskExecutor) :
    ServerConnector<TyeDeploymentConfiguration>() {
    override fun connect(callback: ConnectionCallback<TyeDeploymentConfiguration>) {
        taskExecutor.submit( {
            TyeServerRuntimeInstance(
                configuration,
                taskExecutor
            ).connect(callback)
        }, callback)
    }
}