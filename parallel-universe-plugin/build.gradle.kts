val projectAPI = project(":${rootProject.name}-api")
val projectCORE = project(":${rootProject.name}-core")

dependencies {
    implementation(projectAPI)
}

val pluginName = rootProject.name.split('-').joinToString("") { it.capitalize() }
val packageName = rootProject.name.replace("-", "")
extra.set("pluginName", pluginName)
extra.set("packageName", packageName)

tasks {
    processResources {
        filesMatching("*.yml") {
            expand(project.properties)
            expand(extra.properties)
        }
    }

    fun registerPluginJar(name: String, vararg outputs: Project, configuration: Jar.() -> Unit) = register<Jar>(name) {
        archiveBaseName.set(pluginName)
        archiveVersion.set("")

        outputs.forEach { project ->
            from(project.sourceSets["main"].output)
        }

        configuration()

        doLast {
            copy {
                from(archiveFile)
                val plugins = File(rootDir, ".server/plugins/")
                into(if (File(plugins, archiveFileName.get()).exists()) File(plugins, "update") else plugins)
            }
        }
    }

    registerPluginJar("pluginJar", projectAPI, projectCORE, project) {
        exclude("test.yml")
    }

    findProject(":${rootProject.name}-publish")?.let { publish ->
        registerPluginJar("testJar", project) {
            exclude("plugin.yml")
            rename("test.yml", "plugin.yml")

            publish.tasks.let { tasks ->
                dependsOn(tasks.named("publishApiPublicationToServerRepository"))
                dependsOn(tasks.named("publishCorePublicationToServerRepository"))
            }
        }
    }
}
