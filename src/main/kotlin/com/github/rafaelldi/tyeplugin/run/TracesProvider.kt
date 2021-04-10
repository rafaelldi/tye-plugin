package com.github.rafaelldi.tyeplugin.run

enum class TracesProvider(private val displayName: String, val argumentName: String) {
    NONE("None", "none") {
        override fun isProviderUrlEnabled(): Boolean = false
    },
    ZIPKIN("Zipkin", "zipkin") {
        override fun isProviderUrlEnabled(): Boolean = true
    };

    override fun toString() = displayName

    open fun isProviderUrlEnabled(): Boolean = error("Not implemented for $this")
}
