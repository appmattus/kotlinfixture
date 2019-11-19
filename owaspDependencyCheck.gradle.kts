import org.owasp.dependencycheck.gradle.DependencyCheckPlugin

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.owasp:dependency-check-gradle:5.2.4")
    }
}

subprojects {
    apply<DependencyCheckPlugin>()

    afterEvaluate {
        tasks.getByName("check").dependsOn(tasks.getByName("dependencyCheckAnalyze"))
    }
}
