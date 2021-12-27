package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.remoteServer.util.CloudConfigurationBase

class TyeHostConfiguration : CloudConfigurationBase<TyeHostConfiguration>() {
    var hostAddress = "http://localhost:8000"

    companion object {
        fun createDefault(): TyeHostConfiguration {
            return TyeHostConfiguration()
        }
    }
}