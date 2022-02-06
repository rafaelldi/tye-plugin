package com.github.rafaelldi.tyeplugin.runtimes

import com.github.rafaelldi.tyeplugin.model.*
import com.github.rafaelldi.tyeplugin.services.TyeApplicationManager
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile

class TyeApplicationRuntime(
    applicationName: String,
    val isDeployable: Boolean,
    private val applicationManager: TyeApplicationManager
) :
    TyeBaseRuntime(applicationName) {
    private var processHandler: KillableProcessHandler? = null
    private var model: TyeApplication? = null
    private val serviceRuntimes: MutableMap<String, TyeServiceRuntime<TyeService>> = mutableMapOf()

    fun setUpProcessHandler(handler: KillableProcessHandler) {
        processHandler = handler
    }

    fun isModelNotInitialized(): Boolean = model == null

    fun updateModel(application: TyeApplication) {
        model = application
    }

    fun updateServices(newServices: List<TyeService>) {
        val currentRuntimeNames = serviceRuntimes.keys.toSet()
        val newServiceNames = newServices.map { it.getServiceName() }.toSet()

        for (newService in newServices) {
            val newServiceName = newService.getServiceName()
            if (currentRuntimeNames.contains(newServiceName)) {
                serviceRuntimes[newServiceName]?.updateReplicas(newService)
            } else {
                val newRuntime = createServiceRuntime(newService, this)
                serviceRuntimes[newServiceName] = newRuntime
            }
        }

        for (deletedRuntimeName in currentRuntimeNames.subtract(newServiceNames)) {
            serviceRuntimes.remove(deletedRuntimeName)
        }
    }

    private fun createServiceRuntime(model: TyeService, parent: TyeApplicationRuntime): TyeServiceRuntime<TyeService> =
        when (model) {
            is TyeContainerService -> TyeServiceDockerRuntime(model, parent) as TyeServiceRuntime<TyeService>
            is TyeProjectService -> TyeServiceProjectRuntime(model, parent) as TyeServiceRuntime<TyeService>
            is TyeExecutableService -> TyeServiceExecutableRuntime(model, parent) as TyeServiceRuntime<TyeService>
            is TyeIngressService -> TyeServiceIngressRuntime(model, parent) as TyeServiceRuntime<TyeService>
        }

    fun getServices(): List<TyeServiceRuntime<TyeService>> = serviceRuntimes.values.toList()

    override fun getVirtualFile(): VirtualFile? {
        val path = model?.source ?: return null
        return LocalFileSystem.getInstance().findFileByPath(path)
    }

    override fun isUndeploySupported(): Boolean = true

    override fun undeploy(callback: UndeploymentTaskCallback) {
        processHandler?.killProcess()
    }

    fun shutdownApplication() {
        applicationManager.shutdownApplication()
    }
}