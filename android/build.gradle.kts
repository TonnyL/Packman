import com.android.build.api.dsl.ApkSigningConfig
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

group = "com.lizhaotailang.packman"
version = "1.0-SNAPSHOT"

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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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
                "proguard-rules.pro",
            )
        }
        getByName("debug") {
            isMinifyEnabled = false
        }
    }
    namespace = "com.lizhaotailang.packman.android"
}

kotlin {
    sourceSets {
        enableK2Compiler()
    }
}

dependencies {
    implementation(projects.common)
    implementation(libs.androidx.appcompat)
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.mdc.android)
}
