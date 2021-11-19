package com.github.rafaelldi.tyeplugin.listeners

import com.intellij.util.messages.Topic
import java.util.*

interface TyeGlobalToolListener : EventListener {
    companion object {
        val TOPIC: Topic<TyeGlobalToolListener> =
            Topic.create("Tye global tool topic", TyeGlobalToolListener::class.java)
    }

    fun tyeToolPathChanged(path: String)
}