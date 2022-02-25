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
                if (!credentialFile.exists()) credentialFile.createNewFile()

                val nexusUsername = credentialFile.readLines(StandardCharsets.UTF_8)[0]
                val nexusPassword = credentialFile.readLines(StandardCharsets.UTF_8)[1]

                if (credentialFile.readLines(StandardCharsets.UTF_8).size == 2 && (nexusUsername.isNotBlank() || nexusPassword.isNotBlank())) {
                    logger.info("Current Nexus Username: ${credentialFile.readLines(StandardCharsets.UTF_8)[0]}")
                    logger.info("Current Nexus Password: ${credentialFile.readLines(StandardCharsets.UTF_8)[1]}")

                    username = nexusUsername
                    password = nexusPassword
                }
            }.onFailure {
                logger.warn("Failed to load nexus credentials, make sure the credential file exists, and check the first two lines contains username & password.")
                logger.warn("Other lines will be ignored.")
            }

            url = uri(
                if ("SNAPSHOT" in version as String) {
                    "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                } else {
                    "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                }
            )

            logger.info("Publishing URL: $url")
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
                description.set("Database Manager for Komworld Server.")

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
