import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("kotlinx-serialization")
    id("com.apollographql.apollo3").version(libs.versions.apollo)
    id("com.codingfeline.buildkonfig")
    id("io.realm.kotlin")
}

group = "com.lizhaotailang.packman"
version = "1.0-SNAPSHOT"

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    androidTarget()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = "Shared code for the sample"
        homepage = "https://github.com/TonnyL/Packman"
        ios.deploymentTarget = "16.4"
        podfile = project.file("../iOS/Podfile")
        framework {
            baseName = "common"
            isStatic = true
        }
    }

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
    }
    sourceSets {
        enableK2Compiler()

        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                @OptIn(ExperimentalComposeLibrary::class)
                api(compose.material3)
                api(libs.components.resources)

                api(libs.apollo.runtime)
                api(libs.apollo.adapters)

                api(libs.kotlin.datetime)

                api(libs.ktor.core)
                api(libs.ktor.serialization)
                api(libs.ktor.logging)
                api(libs.ktor.content.negotiation)

                api(libs.realm)
            }
        }

        val androidMain by getting {
            dependencies {
                api(libs.ktor.cio)
                api(compose.preview)

                api(libs.androidx.paging.compose)
                api(libs.androidx.lifecycle.runtime.compose)
                api(libs.androidx.lifecycle.viewmodel.compose)
                api(libs.androidx.navigation.compose)
                api(libs.androidx.lifecycle.viewmodel.ktx)
                api(libs.androidx.activity.compose)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(libs.ktor.cio)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by getting {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                api(libs.ktor.darwin)
                api(libs.ktor.ios)
            }
        }
    }
}

android {
    compileSdk = Versions.compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
    defaultConfig {
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    namespace = "com.lizhaotailang.packman.common"
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
        buildConfigField(
            STRING,
            "ACCESS_TOKEN",
            "${localProperties.getProperty("ACCESS_TOKEN")}"
        )
        buildConfigField(
            STRING,
            "TRIGGER_PIPELINE_ACCESS_TOKEN",
            "${localProperties.getProperty("TRIGGER_PIPELINE_ACCESS_TOKEN")}"
        )
        buildConfigField(
            STRING,
            "PROJECT_PATH",
            "${localProperties.getProperty("PROJECT_PATH")}"
        )
        buildConfigField(
            STRING,
            "GRAPH_QL_SERVER_URL",
            "${localProperties.getProperty("GRAPH_QL_SERVER_URL")}"
        )
        buildConfigField(
            STRING,
            "REST_SERVER_URL",
            "${localProperties.getProperty("REST_SERVER_URL")}"
        )
        buildConfigField(
            STRING,
            "PROJECT_ID",
            "${localProperties.getProperty("PROJECT_ID")}"
        )
    }
}

// run `generateBuildKonfig` for every build.
tasks.build {
    dependsOn("generateBuildKonfig")
}
