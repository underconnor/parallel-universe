import java.nio.charset.StandardCharsets

plugins {
    `maven-publish`
    signing
}

val projectAPI = project(":${rootProject.name}-api")
val projectCORE = project(":${rootProject.name}-core")
val credentialFile = File(rootProject.file("NexusCredentials.txt").toURI())

publishing {
    repositories {
        mavenLocal()

        maven {
            name = "server"
            url = rootProject.uri(".server/libraries")
        }

        maven {
            name = "central"

            credentials.runCatching {
                val nexusUsername = credentialFile.readLines(StandardCharsets.UTF_8)[0]
                val nexusPassword = credentialFile.readLines(StandardCharsets.UTF_8)[1]

                if (nexusUsername.isNotBlank() || nexusPassword.isNotBlank()) {
                    username = nexusUsername
                    password = nexusPassword
                }
            }.onFailure {
                println(credentialFile.readLines(StandardCharsets.UTF_8)[0])
                println(credentialFile.readLines(StandardCharsets.UTF_8)[1])
                logger.warn("Failed to load nexus credentials, make sure the file exists, and check the first line contains username & second line contains password.")
            }

            url = uri(
                if ("SNAPSHOT" in version as String) {
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                } else {
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                }
            )
        }
    }

    publications {
        fun MavenPublication.setup(target: Project) {
            artifactId = target.name
            from(target.components["java"])
            artifact(target.tasks["sourcesJar"])
            artifact(target.tasks["dokkaJar"])

            pom {
                name.set(target.name)
                url.set("https://github.com/komworld/${rootProject.name}")

                licenses {
                    license {
                        name.set("GNU General Public License version 3")
                        url.set("https://opensource.org/licenses/GPL-3.0")
                    }
                }

                developers {
                    developer {
                        id.set("qogusdn1017")
                        name.set("Bae Hyeon Woo")
                        email.set("qogusdn1017@naver.com")
                        url.set("https://github.com/qogusdn1017")
                        roles.addAll("developer")
                        timezone.set("Asia/Seoul")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/komworld/${rootProject.name}.git")
                    developerConnection.set("scm:git:ssh://github.com:komworld/${rootProject.name}.git")
                    url.set("https://github.com/komworld/${rootProject.name}")
                }
            }
        }

        create<MavenPublication>("api") {
            setup(projectAPI)
        }

        create<MavenPublication>("core") {
            setup(projectCORE)
        }
    }
}

signing {
    isRequired = true
    sign(publishing.publications["api"], publishing.publications["core"])
}
