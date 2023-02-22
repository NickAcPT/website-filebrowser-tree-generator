plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    id("org.jetbrains.kotlin.jvm") version "1.8.10"

    // Apply the application plugin to add support for building a CLI application in Java.
    application
    distribution
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Kotlin based CLI library
    implementation("com.github.ajalt.clikt:clikt:3.5.0")

    // Json library
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.+")
}

application {
    mainClass.set("io.github.nickacpt.filebrowser.tree.generator.AppKt")
}
