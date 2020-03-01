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
 * 2) Define $JFX_SDK_DIR$ in your env variables to the project dir /server/javafx-sdk-11.0.2 (or whatever version)
 */

plugins {
	kotlin("jvm")
	application
	id("org.springframework.boot") version "2.1.8.RELEASE"
	id("io.spring.dependency-management") version "1.0.8.RELEASE"
	kotlin("plugin.spring") version "1.3.50"
	kotlin("plugin.jpa") version "1.3.50"
	id("org.jetbrains.kotlin.plugin.serialization") version "1.3.50"
	id("org.openjfx.javafxplugin") version "0.0.7"
	id("com.github.johnrengelman.shadow") version "5.2.0"
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
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	runtimeOnly("com.h2database:h2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.50")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.13.0") // JVM dependency
	implementation("com.github.Hexworks.mixite:mixite.core-jvm:2018.2.0-RELEASE")
	implementation("no.tornado:tornadofx:1.7.17") {
		exclude("org.jetbrains.kotlin")
	}
	implementation("org.hexworks.zircon:zircon.core-jvm:2020.0.2-PREVIEW")
	implementation("org.hexworks.zircon:zircon.jvm.swing:2020.0.2-PREVIEW")

}

application {
	mainClassName = "com.mtw.supplier.ui.EditorApp"
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "1.8"
	}
}
