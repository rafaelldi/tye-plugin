package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.remoteServer.deployment.TyeDeploymentConfiguration
import com.github.rafaelldi.tyeplugin.runtimes.TyeApplicationRuntime
import com.github.rafaelldi.tyeplugin.runtimes.TyeBaseRuntime
import com.intellij.openapi.components.Service
import com.intellij.remoteServer.runtime.deployment.DeploymentTask
import io.ktor.http.*
import java.util.concurrent.ConcurrentHashMap

@Service
class TyeApplicationManager {
    private val applications: ConcurrentHashMap<String, TyeApplicationRuntime> = ConcurrentHashMap()

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

    fun refreshApplication(host: Url): List<TyeBaseRuntime> {
        val existingApplicationRuntime = applications[host.toString()]
        if (existingApplicationRuntime != null) {
            val isApplicationLive = isApplicationLive(existingApplicationRuntime)
            if (!isApplicationLive) {
                applications.remove(host.toString())
                return emptyList()
            }

            return existingApplicationRuntime.refresh()
        }

        val newApplicationRuntime = TyeApplicationRuntime("Tye Application", host)
        val isApplicationLive = isApplicationLive(newApplicationRuntime)
        if (!isApplicationLive) {
            return emptyList()
        }

        val runtimes = newApplicationRuntime.refresh()
        applications[host.toString()] = newApplicationRuntime

        return runtimes
    }

    private fun isApplicationLive(runtime: TyeApplicationRuntime): Boolean {
        val isLive = runtime.isLive()
        val handler = runtime.getProcessHandler()
        return isLive || handler != null
    }

    fun shutdownApplication(host: Url) {
        val existingApplicationRuntime = applications.remove(host.toString())?: return
        existingApplicationRuntime.shutdown()
    }
}
