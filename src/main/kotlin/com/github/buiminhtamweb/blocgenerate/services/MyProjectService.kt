package com.github.buiminhtamweb.blocgenerate.services

import com.github.buiminhtamweb.blocgenerate.MyBundle
import com.intellij.openapi.project.Project

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
