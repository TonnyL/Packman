import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group "com.lizhaotailang.packman"
version "1.0-SNAPSHOT"

android {
    compileSdk = Versions.compileSdk

    val localProperties = gradleLocalProperties(rootDir)
    val hasStoreFilePath = localProperties.getProperty("STORE_FILE_PATH") != null

    defaultConfig {
        applicationId = "com.lizhaotailang.packman.android"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = 1
        versionName = "1.0-SNAPSHOT"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    fun ApkSigningConfig.applySigningConfig() {
        storeFile = rootProject.file(localProperties.getProperty("STORE_FILE_PATH"))
        storePassword = localProperties.getProperty("STORE_PASSWORD")
        keyAlias = localProperties.getProperty("KEY_ALIAS")
        keyPassword = localProperties.getProperty("KEY_PASSWORD")
    }

    val releaseSignConfig = "release"
    println("hasPropertiesFile: $hasStoreFilePath")
    signingConfigs {
        if (hasStoreFilePath) {
            create(releaseSignConfig) {
                applySigningConfig()
            }
        }
        getByName("debug") {
            if (hasStoreFilePath) {
                applySigningConfig()
            }
        }
    }
    buildTypes {
        getByName("release") {
            if (hasStoreFilePath) {
                signingConfig = signingConfigs.getByName(releaseSignConfig)
            }
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":common"))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.material.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.mdc.android)
}
