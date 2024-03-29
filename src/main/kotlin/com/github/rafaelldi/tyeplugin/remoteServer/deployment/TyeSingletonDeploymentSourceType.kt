package com.github.rafaelldi.tyeplugin.remoteServer.deployment

import com.intellij.remoteServer.configuration.deployment.SingletonDeploymentSourceType
import icons.TyeIcons

class TyeSingletonDeploymentSourceType: SingletonDeploymentSourceType("Tye", "Tye", TyeIcons.TYE) {
    companion object{
        fun getInstance(): TyeSingletonDeploymentSourceType {
            return findExtension(TyeSingletonDeploymentSourceType::class.java) as TyeSingletonDeploymentSourceType
        }
    }

    override fun isEditableInDumbMode(): Boolean = true
}