package com.github.rafaelldi.tyeplugin.cli.builders

class TyeInitCliBuilder(private val path: String) {
    private val arguments: MutableList<String> = mutableListOf()

    init {
        arguments.add("init")
    }

    fun setForce() {
        arguments.add("--force")
    }

    fun build(): List<String> {
        arguments.add(path)
        return arguments
    }
}
