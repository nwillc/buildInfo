package com.github.nwillc.buildInfo

import groovy.json.JsonOutput
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Paths

class BuildInfoPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.extensions.create("buildinfo", BuildInfoPlugin.Extension)
        project.task('buildInfo') << {
            def buildInfo = [:]
            buildInfo['project.name'] = project.name
            buildInfo['project.version'] = project.version
            buildInfo['builder'] = System.getProperty("user.name")
            buildInfo['build.date'] = new Date()
            buildInfo['java.vendor'] = System.getProperty("java.vendor")
            buildInfo['java.version'] = System.getProperty("java.version")
            buildInfo['source.compatibility'] = project.sourceCompatibility.toString()
            buildInfo['target.compatibility'] = project.targetCompatibility.toString()
            def dependencies = []
            project.sourceSets.main.runtimeClasspath.each { item ->
                if (item.getName().endsWith(".jar")) {
                    dependencies.add(item.getName())
                }
            }
            buildInfo['dependencies'] = dependencies
            if (project.buildinfo.custom.size() > 0) {
                buildInfo['custom'] = project.buildinfo.custom
            }
            println "Generating " + project.buildinfo.output
            new File(project.buildinfo.output).write JsonOutput.prettyPrint(JsonOutput.toJson(buildInfo))
        }
    }

    static class Extension {
        String output = Paths.get("src", "main", "resources", "build.json").toString()
        Map<String,String> custom = new HashMap<>();
    }
}