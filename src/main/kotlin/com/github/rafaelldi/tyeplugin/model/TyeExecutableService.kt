package com.github.rafaelldi.tyeplugin.model

class TyeExecutableService(
    properties: MutableMap<String, String?>,
    bindings: List<TyeServiceBinding>,
    environmentVariables: MutableMap<String, String?>
) : TyeService(properties, bindings, environmentVariables, null) {
    override fun getName(): String = properties[PROPERTY_ID_KEY] ?: "executable"
}