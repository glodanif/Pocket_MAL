buildscript {
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.2")
        classpath(kotlin("gradle-plugin", version = "1.9.25"))
        classpath("com.google.android.gms:oss-licenses-plugin:0.10.6")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = java.net.URI("https://jitpack.io") }
    }
}

plugins {
    id("com.google.devtools.ksp") version "1.9.25-1.0.20" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
