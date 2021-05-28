package com.github.rafaelldi.tyeplugin.model

sealed class Service(val properties: Properties, val serviceBindings: List<ServiceBinding>) {
    class External(properties: Properties, serviceBindings: List<ServiceBinding>) :
        Service(properties, serviceBindings)

    class Project(properties: Properties, serviceBindings: List<ServiceBinding>) :
        Service(properties, serviceBindings)

    class Executable(properties: Properties, serviceBindings: List<ServiceBinding>) :
        Service(properties, serviceBindings)

    class Container(properties: Properties, serviceBindings: List<ServiceBinding>) :
        Service(properties, serviceBindings)

    class Function(properties: Properties, serviceBindings: List<ServiceBinding>) :
        Service(properties, serviceBindings)

    class Ingress(properties: Properties, serviceBindings: List<ServiceBinding>) :
        Service(properties, serviceBindings)
}
