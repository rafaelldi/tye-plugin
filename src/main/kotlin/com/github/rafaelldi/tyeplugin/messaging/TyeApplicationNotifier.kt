package com.github.rafaelldi.tyeplugin.messaging

import com.intellij.util.messages.Topic

interface TyeApplicationNotifier {
    companion object {
        val TOPIC: Topic<TyeApplicationNotifier> = Topic.create("Tye Application", TyeApplicationNotifier::class.java)
    }

    fun connectedToTyeHost()
    fun tyeApplicationUpdated()
    fun tyeApplicationStopped()
}
