import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.devtools.ksp")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

var apikeyPropertiesFile = rootProject.file("apikey.properties")
var apikeyProperties = Properties()
apikeyProperties.load(apikeyPropertiesFile.inputStream())

android {
    namespace = "com.madteam.sunset"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.madteam.sunset"
        minSdk = 26
        targetSdk = 34
        versionCode = 8
        versionName = "0.2.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField(
            "String",
            "GOOGLE_MAPS_API_KEY",
            apikeyProperties["GOOGLE_MAPS_API_KEY"].toString()
        )
        buildConfigField(
            "String",
            "GOOGLE_ADMOB_KEY",
            apikeyProperties["GOOGLE_ADMOB_KEY"].toString()
        )
        buildConfigField(
            "String",
            "WEATHER_API_KEY",
            apikeyProperties["WEATHER_API_KEY"].toString()
        )
        buildConfigField(
            "String",
            "WEB_ID_CLIENT",
            apikeyProperties["WEB_ID_CLIENT"].toString()
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        resources.excludes.add("google/protobuf/*.proto")
    }
}

dependencies {
    // Kotlin
    implementation(libs.androidx.core.ktx)

    // Google
    implementation(libs.google.services)
    implementation(libs.play.services.auth)

    // Retrofit
    implementation(libs.retrofit)

    // GSON Converter
    implementation(libs.converter.gson)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.constraintlayout)
    ksp(libs.hilt.compiler)

    // Firebase
    implementation(platform(libs.com.google.firebase.bom))
    implementation(libs.com.google.firebase.auth.ktx)
    implementation(libs.com.google.firebase.analytics.ktx)
    implementation(libs.com.google.firebase.crashlytics.ktx)
    implementation(libs.com.google.firebase.firestore.ktx)
    implementation(libs.com.google.firebase.storage.ktx)
    implementation(libs.com.google.firebase.config.ktx)
    implementation(libs.com.google.firebase.messaging.ktx)
    implementation(libs.com.google.firebase.database.ktx)

    // Google AdMob
    implementation(libs.play.services.ads)

    //Google Play App Updates
    implementation(libs.play.app.updates)
    implementation(libs.play.app.updates.ktx)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.accompanist.pager)

    // Compose Previews
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.tooling)
    implementation(libs.accompanist.navigation.animation)

    // Material
    implementation(libs.compose.material)
    implementation(libs.compose.material.icons)
    implementation(libs.androidx.material3)
    implementation(libs.material)

    // Google Maps
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)
    implementation(libs.maps.compose)

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // KTX for the Maps SDK for Android
    implementation(libs.maps.ktx)
    // KTX for the Maps SDK for Android Utility Library
    implementation(libs.maps.utils.ktx)

    // Lottie
    implementation(libs.lottie.compose)

    // CanaryLeak
    debugImplementation(libs.leakcanary.android)

    // Glide for compose
    implementation(libs.glide)

    // AppCompat
    implementation(libs.androidx.appcompat)

    // Datastore
    implementation(libs.androidx.datastore.preferences)

    // Permissions
    implementation(libs.accompanist.permissions)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
