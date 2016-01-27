Gradle Build Info Plugin
========================

This plugin will generate a JSON file with Java build information in it. It's intended use is to generate the file
each build so it can be tucked into the project resources which can be accessed at runtime, for example allowing a server 
to report some of the conditions under which the build occurred.  The JSON file contains minimal project information, 
Java version information, dependencies, build date etc.  The output file name can be changed, and custom values can be 
added.

For usage info see: 
[![Gradle Plugin Site](http://shields-nwillc.rhcloud.com/shield/gradle_plugin?path=com.github.nwillc&package=buildinfo)]
(http://shields-nwillc.rhcloud.com/homepage/gradle_plugin?group=com.github.nwillc&package=buildinfo)


## Notes

The plugin provides the task `buildInfo` which can be invoked:

    gradle buildInfo

An example build.gradle snippet configuring the task (it has sane defaults without this) would look as follows:

    // The configuration is optional - the plugin has sane defaults
    buildinfo {
        output = 'src/main/resources/build.json'  
        custom['branch'] = 'dev'
    }
    
    processResources.dependsOn buildInfo  // Run each time the resources are updated
    
The above would create the JSON file `src/main/resources/build.json` before each build with contents something like:

    {
        "build.date": "2015-12-30T20:13:06+0000",
        "custom": {
                    "branch": "dev"
                },
        "dependencies": [
            "slf4j-jdk14-1.7.13.jar",
            "jetty-server-9.3.2.v20150730.jar",
            "jetty-webapp-9.3.2.v20150730.jar",
            "websocket-server-9.3.2.v20150730.jar",
            "websocket-servlet-9.3.2.v20150730.jar",
            "javax.servlet-api-3.1.0.jar",
            "jetty-http-9.3.2.v20150730.jar",
            "jetty-io-9.3.2.v20150730.jar"
        ],
        "java.vendor": "Oracle Corporation",
        "java.version": "1.8.0_60",     
        "project.name": "super-server",
        "project.version": "1.0.5",
        "source.compatibility": "1.8",
        "target.compatibility": "1.8"
    }
    
-----
[![ISC License](http://shields-nwillc.rhcloud.com/shield/tldrlegal?package=ISC)](http://shields-nwillc.rhcloud.com/homepage/tldrlegal?package=ISC)
[![Build Status](http://shields-nwillc.rhcloud.com/shield/travis-ci?path=nwillc&package=buildInfo)](http://shields-nwillc.rhcloud.com/homepage/travis-ci?path=nwillc&package=buildInfo)
