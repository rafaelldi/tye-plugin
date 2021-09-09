package com.github.rafaelldi.tyeplugin.runConfig.tyeRun

@Suppress("unused")
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
