import org.owasp.dependencycheck.gradle.DependencyCheckPlugin
import org.owasp.dependencycheck.gradle.extension.DependencyCheckExtension

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.owasp:dependency-check-gradle:5.3.2.1")
    }
}

subprojects {
    apply<DependencyCheckPlugin>()

    configure<DependencyCheckExtension> {
        failBuildOnCVSS = 0f

        suppressionFile = file("$rootDir/cve-suppressions.xml").toString()

        analyzers.assemblyEnabled = false

        skipConfigurations = listOf(
            "lintClassPath",
            "jacocoAgent",
            "jacocoAnt",
            "kotlinCompilerClasspath",
            "kotlinCompilerPluginClasspath"
        )
    }

    afterEvaluate {
        tasks.named("check") {
            dependsOn(tasks.named("dependencyCheckAnalyze"))
        }
    }
}
