package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.remoteServer.runtime.Deployment

class TyeDeploymentNodeComponentProvider {
    private val map: MutableMap<Deployment, TyeDeploymentNodeComponent> = mutableMapOf()

    fun getComponent(deployment: Deployment): TyeDeploymentNodeComponent =
        map.computeIfAbsent(deployment, ::TyeDeploymentNodeComponent)
}