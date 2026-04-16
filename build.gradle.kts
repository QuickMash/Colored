plugins {
    id("java-library")
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

val releaseVersionOrNull = providers.gradleProperty("releaseVersion")
    .orElse(providers.environmentVariable("GITHUB_REF_NAME"))
    .orNull

// Prefer explicit Gradle property for local release checks, then fall back to CI tag env.
if (!releaseVersionOrNull.isNullOrBlank()) {
    version = releaseVersionOrNull
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.11-R0.1-SNAPSHOT")
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
        dependsOn("jar")

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
}
