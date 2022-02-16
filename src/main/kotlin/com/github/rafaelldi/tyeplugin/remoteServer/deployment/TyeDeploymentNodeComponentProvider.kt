package com.github.rafaelldi.tyeplugin.remoteServer.deployment

import com.github.rafaelldi.tyeplugin.remoteServer.components.*
import com.github.rafaelldi.tyeplugin.runtimes.*
import com.intellij.openapi.project.Project
import com.intellij.remoteServer.runtime.ConnectionStatus
import com.intellij.remoteServer.runtime.Deployment
import com.intellij.remoteServer.runtime.ServerConnection
import com.intellij.remoteServer.runtime.ServerConnectionListener

class TyeDeploymentNodeComponentProvider(private val project: Project) {
    private val map: MutableMap<Deployment, TyeDeploymentNodeComponent> = mutableMapOf()

    init {
        project.messageBus.connect().subscribe(ServerConnectionListener.TOPIC, object : ServerConnectionListener {
            override fun onConnectionCreated(connection: ServerConnection<*>) {
            }

            override fun onConnectionStatusChanged(connection: ServerConnection<*>) {
                if (connection.status == ConnectionStatus.DISCONNECTED) {
                    map.filterKeys { it.connection == connection }
                        .forEach { map.remove(it.key) }
                }
            }

            override fun onDeploymentsChanged(connection: ServerConnection<*>) {
            }
        })
    }

    fun getComponent(deployment: Deployment): TyeDeploymentNodeComponent =
        map.computeIfAbsent(deployment, this::createComponent)

    private fun createComponent(deployment: Deployment): TyeDeploymentNodeComponent {
        return when (val runtime = deployment.runtime) {
            is TyeApplicationRuntime -> ApplicationDeploymentNodeComponent(project, runtime)
            is TyeServiceProjectRuntime -> ServiceDeploymentNodeComponent(runtime)
            is TyeServiceDockerRuntime -> ServiceDeploymentNodeComponent(runtime)
            is TyeServiceExecutableRuntime -> ServiceDeploymentNodeComponent(runtime)
            is TyeServiceIngressRuntime -> ServiceDeploymentNodeComponent(runtime)
            is TyeReplicaRuntime<*> -> ReplicaDeploymentNodeComponent(runtime)
            else -> EmptyDeploymentNodeComponent()
        }
    }

    fun updateComponent(deployment: Deployment) {
        val component = map[deployment]
        component?.update()
    }
}