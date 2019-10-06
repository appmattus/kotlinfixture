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

    // To override MaxLineLength:excludeCommentStatements
    // config = files("detekt-config.yml")
}

println(System.getenv("GITHUB_REPOSITORY")) // appmattus/kotlinfixture
  // appmattus/kotlinfixture
println(System.getenv("GITHUB_EVENT_NAME")) // push
  // pull_request
println(System.getenv("GITHUB_SHA")) // 283a30f046672e7df9ef67885ef29b66b8b12ade
  // 59888119b32c8b239c6ee411fa2388d249676ddf
println(System.getenv("GITHUB_REF")) // refs/heads/master
  // refs/pull/9/merge
println(System.getenv("CI_BRANCH")) // ?    on a PR CI_BRANCH: refs/pull/9/merge
  // refs/pull/9/merge
println(System.getenv("CI_PULL_REQUEST")) // ?   blank on a PR
  // ?
