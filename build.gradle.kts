import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.3.50" apply false
    id("com.github.ben-manes.versions") version "0.25.0"
    id("io.gitlab.arturbosch.detekt") version "1.0.1"
}

apply(from = "$rootDir/owaspDependencyCheck.gradle.kts")

allprojects {
    repositories {
        jcenter()
        mavenCentral()
    }
}

task("clean", type = Delete::class) {
    delete(rootProject.buildDir)
}

tasks.withType(DependencyUpdatesTask::class.java).all {
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview")
                    .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-]*") }
                    .any { it.matches(candidate.version) }

                if (rejected) {
                    reject("Release candidate")
                }
            }
        }
    }
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.0.1")
}

detekt {
    input = files("$projectDir")

    buildUponDefaultConfig = true

    config = files("detekt-config.yml")
}
