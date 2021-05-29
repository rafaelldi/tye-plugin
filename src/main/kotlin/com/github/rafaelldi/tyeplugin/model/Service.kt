package com.github.rafaelldi.tyeplugin.model

sealed class Service(
    val properties: Properties,
    val bindings: List<Binding>,
    val environmentVariables: List<EnvironmentVariable>
) {
    class External(properties: Properties, bindings: List<Binding>, envVars: List<EnvironmentVariable>) :
        Service(properties, bindings, envVars)

    class Project(properties: Properties, bindings: List<Binding>, envVars: List<EnvironmentVariable>) :
        Service(properties, bindings, envVars)

    class Executable(properties: Properties, bindings: List<Binding>, envVars: List<EnvironmentVariable>) :
        Service(properties, bindings, envVars)

    class Container(properties: Properties, bindings: List<Binding>, envVars: List<EnvironmentVariable>) :
        Service(properties, bindings, envVars)

    class Function(properties: Properties, bindings: List<Binding>, envVars: List<EnvironmentVariable>) :
        Service(properties, bindings, envVars)

    class Ingress(properties: Properties, bindings: List<Binding>, envVars: List<EnvironmentVariable>) :
        Service(properties, bindings, envVars)
}
