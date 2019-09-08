plugins {
    kotlin("jvm") version "1.3.50" apply false
}

subprojects {
    version = "1.0"
}

allprojects {
    repositories {
        jcenter()
    }
}