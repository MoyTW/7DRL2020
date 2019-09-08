import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * To set TornadoFX to run properly in IntelliJ:
 *
 * 1) Place the following line in the Run Configuration VM Options
 * --module-path "$JFX_SDK_DIR$\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.base,javafx.graphics
 *
 * Below...seems to work, somehow? without the JFX_SDK_DIR?
 * --module-path "$MODULE_DIR$\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.base,javafx.graphics --add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED
 *
 * 2) Define $JFX_SDK_DIR$ in your env variables to the project dir /editor/javafx-sdk-11.0.2 (or whatever version)
 */


plugins {
    kotlin("jvm")
    id("org.jetbrains.kotlin.plugin.serialization") version "1.3.50"
    id("org.openjfx.javafxplugin") version "0.0.7"
}

javafx {
    version = "12"
    modules = listOf("javafx.controls", "javafx.base", "javafx.fxml", "javafx.graphics")
}

group = "com.mtw"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val developmentOnly by configurations.creating
configurations {
    runtimeClasspath {
        extendsFrom(developmentOnly)
    }
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io")
}

dependencies {
    compile(project(":server"))
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.50")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0") // JVM dependency
    implementation("com.github.Hexworks.mixite:mixite.core-jvm:2018.2.0-RELEASE")
    implementation("no.tornado:tornadofx:1.7.17") {
        exclude("org.jetbrains.kotlin")
    }
    implementation("io.github.rybalkinsd:kohttp:0.11.1")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}
