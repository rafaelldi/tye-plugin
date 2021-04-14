package com.github.rafaelldi.tyeplugin.runConfig.tyeRun

enum class LogsProvider(private val displayName: String, val argumentName: String) {
    NONE("None", "none") {
        override fun isProviderUrlEnabled(): Boolean = false
    },
    CONSOLE("Console", "console") {
        override fun isProviderUrlEnabled(): Boolean = false
    },
    SEQ("Seq", "seq") {
        override fun isProviderUrlEnabled(): Boolean = true
    },
    ELASTICSEARCH("ElasticSearch", "elastic") {
        override fun isProviderUrlEnabled(): Boolean = true
    },
    APPLICATIONINSIGHTS("ApplicationInsights", "ai") {
        override fun isProviderUrlEnabled(): Boolean = true
    };

    override fun toString() = displayName

    open fun isProviderUrlEnabled(): Boolean = error("Not implemented for $this")
}
