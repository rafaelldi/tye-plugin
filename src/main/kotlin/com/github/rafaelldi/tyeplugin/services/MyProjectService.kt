package com.github.rafaelldi.tyeplugin.services

import com.github.rafaelldi.tyeplugin.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
