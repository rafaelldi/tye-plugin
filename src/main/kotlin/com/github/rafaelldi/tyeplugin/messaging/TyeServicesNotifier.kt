package com.github.rafaelldi.tyeplugin.messaging

import com.intellij.util.messages.Topic

interface TyeServicesNotifier {
    companion object {
        val TOPIC: Topic<TyeServicesNotifier> = Topic.create("Tye services Updated", TyeServicesNotifier::class.java)
    }

    fun tyeServicesUpdated()
}
