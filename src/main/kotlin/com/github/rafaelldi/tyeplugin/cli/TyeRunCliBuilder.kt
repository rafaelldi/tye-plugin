package com.github.rafaelldi.tyeplugin.cli

class TyeRunCliBuilder(private val path: String) {
    private val arguments: MutableList<String> = mutableListOf()

    init {
        arguments.add("run")
    }

    fun setPort(port: Int) {
        arguments.add("--port")
        arguments.add(port.toString())
    }

    fun setNoBuild() {
        arguments.add("--no-build")
    }

    fun setDocker() {
        arguments.add("--docker")
    }

    fun setDashboard() {
        arguments.add("--dashboard")
    }

    fun setVerbosity(verbosity: String) {
        arguments.add("--verbosity")
        arguments.add(verbosity)
    }

    fun setTags(tags: String) {
        arguments.add("--tags")
        arguments.add(tags)
    }

    fun setLogs(provider: String, url: String?) {
        arguments.add("--logs")
        var providerArg = provider
        if (!url.isNullOrBlank()) providerArg += "=$url"
        arguments.add(providerArg)
    }

    fun setTraces(provider: String, url: String?) {
        arguments.add("--dtrace")
        var providerArg = provider
        if (!url.isNullOrBlank()) providerArg += "=$url"
        arguments.add(providerArg)
    }

    fun build(): List<String> {
        arguments.add(path)
        return arguments
    }
}
