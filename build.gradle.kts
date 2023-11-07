buildscript {
    dependencies {
        classpath(libs.google.services)
        classpath(libs.secrets.gradle.plugin)
    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.android.library") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.dagger.hilt.android") version "2.48.1" apply false
    id("com.google.firebase.crashlytics") version "2.9.4" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    id("org.jetbrains.kotlinx.kover") version "0.4.3"
}