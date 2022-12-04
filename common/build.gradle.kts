import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.apollographql.apollo3").version(libs.versions.apollo)
    id("com.codingfeline.buildkonfig")
}

group = "com.lizhaotailang.packman"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.apollo.runtime)
                api(libs.apollo.adapters)

                api(libs.kotlin.datetime)

                api(libs.ktor.core)
                api(libs.ktor.serialization)
                api(libs.ktor.logging)
                api(libs.ktor.content.negotiation)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                @OptIn(ExperimentalComposeLibrary::class)
                api(compose.material3)
                api(compose.preview)

                api(libs.ktor.cio)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(libs.junit4)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                @OptIn(ExperimentalComposeLibrary::class)
                api(compose.material3)
                api(compose.preview)

                api(libs.ktor.cio)
            }
        }
        val desktopTest by getting
    }
}

android {
    compileSdk = Versions.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

apollo {
    service("gitlab") {
        // download the schema file from https://docs.github.com/public/schema.docs.graphql
        // and then change the extension name of the file from `graphql` to `sdl`.
        schemaFile.set(File("src/commonMain/graphql/schema.graphqls"))
        packageName.set("com.lizhaotailang.packman.graphql")
        failOnWarnings.set(false)
        useSemanticNaming.set(true)
        generateAsInternal.set(false)
        generateApolloMetadata.set(true)
        srcDir(file("src/commonMain/graphql/"))
        mapScalar("Time", "kotlinx.datetime.Instant")
        mapScalar("JobID", "kotlin.String")
    }
}

buildkonfig {
    packageName = "com.lizhaotailang.packman.common"
    objectName = "BuildConfig"
    exposeObjectWithName = "CommonBuildConfig"

    val localProperties = gradleLocalProperties(rootDir)
    defaultConfigs {
        buildConfigField(STRING, "ACCESS_TOKEN", "${localProperties.getProperty("ACCESS_TOKEN")}")
        buildConfigField(
            STRING,
            "PROJECT_ACCESS_TOKEN",
            "${localProperties.getProperty("PROJECT_ACCESS_TOKEN")}"
        )
    }
}

// run `generateBuildKonfig` for every build.
tasks.build {
    dependsOn("generateBuildKonfig")
}
