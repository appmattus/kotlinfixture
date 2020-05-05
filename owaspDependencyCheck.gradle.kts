import org.owasp.dependencycheck.gradle.DependencyCheckPlugin

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.owasp:dependency-check-gradle:5.3.2")
    }
}

subprojects {
    apply<DependencyCheckPlugin>()

    afterEvaluate {
        tasks.getByName("check").dependsOn(tasks.getByName("dependencyCheckAnalyze"))
    }
}
