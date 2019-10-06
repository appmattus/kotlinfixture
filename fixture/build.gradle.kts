import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("jacoco")
    id("com.github.kt3k.coveralls")
}

apply(from = "$rootDir/bintray.gradle.kts")

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.github.classgraph:classgraph:4.8.47")
    implementation(kotlin("reflect"))

    testImplementation("junit:junit:4.12")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

// Fix lack of source code when publishing pure Kotlin projects
// See https://github.com/novoda/bintray-release/issues/262
tasks.whenTaskAdded {
    if (name == "generateSourcesJarForMavenPublication") {
        this as Jar
        from(sourceSets.main.get().allSource)
    }
}

tasks.getByName("test").finalizedBy(tasks.getByName("jacocoTestReport"))

tasks.withType<JacocoReport> {
    reports {
        xml.isEnabled = true
        html.isEnabled = true
    }
}

coveralls {
    sourceDirs = sourceSets.main.get().allSource.srcDirs.map { it.path }
    jacocoReportPath = "$buildDir/reports/jacoco/test/jacocoTestReport.xml"
}

tasks.getByName("jacocoTestReport").finalizedBy(tasks.getByName("coveralls"))

tasks.getByName("coveralls").onlyIf { System.getenv("CI")?.isNotEmpty() == true }
