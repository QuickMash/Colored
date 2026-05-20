import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.3.1"
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("io.papermc.hangar-publish-plugin") version "0.1.2"
}

/**
 * Version strategy:
 * 1) -PreleaseVersion=... (manual override)
 * 2) GitHub Actions unique run id: <run_id>.<run_attempt>
 * 3) Fallback for local/dev
 */
val releaseVersionOrNull = providers.gradleProperty("releaseVersion")
    .orElse(
        providers.environmentVariable("GITHUB_RUN_ID").zip(
            providers.environmentVariable("GITHUB_RUN_ATTEMPT")
        ) { runId, attempt ->
            "$runId.$attempt"
        }
    )
    .orElse("dev-${System.currentTimeMillis()}")
    .orNull

if (!releaseVersionOrNull.isNullOrBlank()) {
    version = releaseVersionOrNull
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.2.1")
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

tasks {
    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.21.11")
        jvmArgs("-Xms2G", "-Xmx2G", "-Dcom.mojang.eula.agree=true")
    }

    register("verifySingleReleaseJar") {
        dependsOn("shadowJar")

        doLast {
            val excludedSuffixes = setOf("-sources.jar", "-javadoc.jar", "-dev.jar", "-plain.jar")
            val jars = layout.buildDirectory.dir("libs").get().asFile
                .listFiles { file -> file.isFile && file.name.endsWith(".jar") }
                ?.toList()
                .orEmpty()
            val publishable = jars.filterNot { jar -> excludedSuffixes.any { jar.name.endsWith(it) } }

            check(publishable.size == 1) {
                "Expected exactly one publishable JAR in build/libs, found ${publishable.size}: ${
                    publishable.joinToString { it.name }
                }"
            }
        }
    }

    processResources {
        val props = mapOf("version" to version, "description" to project.description)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    named<ShadowJar>("shadowJar") {
        configurations = listOf(project.configurations.runtimeClasspath.get())

        dependencies {
            include(dependency("org.bstats:bstats-bukkit:.*"))
        }

        relocate("org.bstats", "${project.group}.bstats")
        archiveClassifier.set("")
    }

    jar {
        archiveClassifier.set("plain")
    }
}

hangarPublish {
    publications.register("plugin") {
        version.set(project.version.toString())
        channel.set("Snapshot")
        id.set("QuickMash/Colored")
        apiKey.set(providers.environmentVariable("HANGAR_API_TOKEN"))

        platforms {
            paper {
                jar.set(tasks.named<ShadowJar>("shadowJar").flatMap { it.archiveFile })
                platformVersions.set(listOf("1.21.11"))
            }
        }
    }
}
