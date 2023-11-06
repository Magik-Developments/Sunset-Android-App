import java.util.Properties

plugins {
    kotlin("kapt")
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
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
        minSdk = 24
        targetSdk = 34
        versionCode = 6
        versionName = "0.1.4.1"

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
    }
}

dependencies {
    // Kotlin
    implementation("androidx.core:core-ktx:1.12.0")

    // Google
    implementation("com.google.gms:google-services:4.3.15")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // GSON Converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    kapt("com.google.dagger:hilt-compiler:2.48.1")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-messaging-ktx")
    implementation("com.google.firebase:firebase-database-ktx")

    // Google AdMob
    implementation("com.google.android.gms:play-services-ads:22.4.0")

    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.navigation:navigation-compose:2.5.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("com.google.accompanist:accompanist-pager:0.22.0-rc")

    // Compose Previews
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.30.1")

    // Material
    implementation("androidx.compose.material:material")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3:1.2.0-alpha09")
    implementation("com.google.android.material:material:1.10.0")

    // Google Maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.maps.android:maps-compose:2.8.0")

    // Room
    implementation("androidx.room:room-ktx:2.6.0")
    kapt("androidx.room:room-compiler:2.6.0")

    // KTX for the Maps SDK for Android
    implementation("com.google.maps.android:maps-ktx:3.2.1")
    // KTX for the Maps SDK for Android Utility Library
    implementation("com.google.maps.android:maps-utils-ktx:3.2.1")

    // Lottie
    implementation("com.airbnb.android:lottie-compose:6.0.0")

    // CanaryLeak
    debugImplementation("com.squareup.leakcanary:leakcanary-android:2.10")

    // Glide for compose
    implementation("com.github.bumptech.glide:compose:1.0.0-alpha.1")

    // AppCompat
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Datastore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Permissions
    implementation("com.google.accompanist:accompanist-permissions:0.30.1")

    // Tests
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
