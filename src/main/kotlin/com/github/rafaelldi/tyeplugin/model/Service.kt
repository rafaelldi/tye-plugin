package com.github.rafaelldi.tyeplugin.model

sealed class Service(val properties: Properties) {
    class External(properties: Properties) : Service(properties)

    class Project(properties: Properties) : Service(properties)

    class Executable(properties: Properties) : Service(properties)

    class Container(properties: Properties) : Service(properties)

    class Function(properties: Properties) : Service(properties)

    class Ingress(properties: Properties) : Service(properties)
}
