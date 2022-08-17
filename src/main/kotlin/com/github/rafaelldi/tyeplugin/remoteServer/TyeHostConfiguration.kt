package com.github.rafaelldi.tyeplugin.remoteServer

import com.intellij.remoteServer.util.CloudConfigurationBase
import io.ktor.http.*

class TyeHostConfiguration : CloudConfigurationBase<TyeHostConfiguration>() {
    var hostAddress = "http://localhost:8000"
    var monitorMetrics = false

    companion object {
        fun createDefault(): TyeHostConfiguration {
            return TyeHostConfiguration()
        }
    }
}

val TyeHostConfiguration.hostUrl: Url
    get() = Url(hostAddress)

