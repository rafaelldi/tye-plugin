package com.github.rafaelldi.tyeplugin.util

class ToolVersion(versionString: String) : Comparable<ToolVersion> {
    private val parts: List<Int> = versionString
        .split("-")[0]
        .split(".")
        .map { it.toInt() }

    override fun compareTo(other: ToolVersion): Int {
        for (i in 0..2) {
            if (this.parts[i] > other.parts[i]) return 1
            if (this.parts[i] < other.parts[i]) return -1
        }
        return 0
    }

    override fun toString(): String = parts.joinToString(".", "v. ")
}
