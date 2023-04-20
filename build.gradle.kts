import org.jetbrains.kotlin.gradle.dsl.KotlinVersion
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

group = "com.lizhaotailang.packman"
version = "1.0-SNAPSHOT"

buildscript {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.compose.multiplatformPlugin)
        classpath(libs.kotlin.serialization.gradlePlugin)
        classpath(libs.build.konfig)
        classpath(libs.realm.gradlePlugin)
    }
}

allprojects {
    apply(plugin = "com.lizhaotailang.packman.spotless")

    tasks.withType<KotlinCompilationTask<*>>()
        .configureEach {
            compilerOptions {
                languageVersion.set(KotlinVersion.KOTLIN_1_9)
                freeCompilerArgs.add("-Xskip-prerelease-check")
            }
        }
}
