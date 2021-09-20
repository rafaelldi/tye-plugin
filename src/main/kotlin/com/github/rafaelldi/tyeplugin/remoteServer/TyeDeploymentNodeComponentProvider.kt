package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.remoteServer.runtime.Deployment

class TyeDeploymentNodeComponentProvider {
    private var component: TyeDeploymentNodeComponent? = null

    fun getComponent(deployment: Deployment): TyeDeploymentNodeComponent {
        if (component == null) {
            component = TyeDeploymentNodeComponent(deployment)
        }

        return component!!
    }
}