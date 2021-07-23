package com.github.rafaelldi.tyeplugin.settings

import com.github.rafaelldi.tyeplugin.remoteServer.TyeHostType
import com.intellij.remoteServer.configuration.RemoteServersManager
import com.intellij.remoteServer.impl.configuration.RemoteServerListConfigurable

class TyeRemoteServerConfigurable : RemoteServerListConfigurable(
    RemoteServersManager.getInstance(),
    listOf(TyeHostType.getInstance()),
    null as String?
)