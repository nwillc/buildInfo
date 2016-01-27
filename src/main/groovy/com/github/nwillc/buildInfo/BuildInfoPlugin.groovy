/*
 * Copyright (c) 2016, nwillc@gmail.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 * ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 * OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.github.nwillc.buildInfo

import groovy.json.JsonOutput
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Paths

class BuildInfoPlugin implements Plugin<Project> {
    private static final String[] SYSTEM_PROPERTIES = ["user.name", "java.vendor", "java.version", "os.arch", "os.name", "os.version"]

    @Override
    void apply(Project project) {
        project.extensions.create("buildinfo", BuildInfoPlugin.Extension)
        project.task('buildInfo') << {
            def buildInfo = new TreeMap<String,Object>()
            buildInfo['project.name'] = project.name
            buildInfo['project.version'] = project.version
            buildInfo['build.date'] = new Date()
            buildInfo['source.compatibility'] = project.sourceCompatibility.toString()
            buildInfo['target.compatibility'] = project.targetCompatibility.toString()
            SYSTEM_PROPERTIES.each { buildInfo[it] = System.getProperty(it) }
            def dependencies = new TreeSet<String>()
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
        Map<String,Object> custom = new TreeMap<>();
    }
}