package com.github.rafaelldi.tyeplugin.listeners

import com.intellij.util.messages.Topic

interface TyeServicesNotifier {
    companion object {
        val TOPIC: Topic<TyeServicesNotifier> = Topic.create("Services Updated", TyeServicesNotifier::class.java)
    }

    fun servicesUpdated()
}
