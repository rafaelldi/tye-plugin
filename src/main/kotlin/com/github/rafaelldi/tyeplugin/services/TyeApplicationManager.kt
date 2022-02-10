package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.remoteServer.deployment.TyeDeploymentConfiguration
import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.github.rafaelldi.tyeplugin.runtimes.TyeBaseRuntime
import com.intellij.openapi.components.Service
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import io.ktor.http.*

@Service
class TyeApplicationManager {
    private val applications: MutableMap<String, TyeApplicationRuntime> = mutableMapOf()

    fun runApplication(host: Url, deploymentTask: DeploymentTask<TyeDeploymentConfiguration>): TyeApplicationRuntime {
        val existingApplicationRuntime = applications[host.toString()]
        if (existingApplicationRuntime != null) {
            return existingApplicationRuntime
        }

        val newApplicationRuntime = TyeApplicationRuntime("Tye Application", host)
        newApplicationRuntime.run(deploymentTask)
        applications[host.toString()] = newApplicationRuntime

        return newApplicationRuntime
    }

    fun updateApplication(host: Url): List<TyeBaseRuntime> {
        val existingApplicationRuntime = applications[host.toString()]
        if (existingApplicationRuntime != null) {
            return existingApplicationRuntime.update()
        }

        val newApplicationRuntime = TyeApplicationRuntime("Tye Application", host)
        return newApplicationRuntime.update()
    }

    fun shutdownApplication(host: Url) {
        val existingApplicationRuntime = applications[host.toString()] ?: return
        existingApplicationRuntime.shutdownApplication()
        applications.remove(host.toString())
    }
}
