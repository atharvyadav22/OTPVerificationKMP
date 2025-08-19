import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    id("com.vanniktech.maven.publish") version "0.34.0"
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        publishLibraryVariants("release")
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm()

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.play.services.auth)
            implementation(libs.play.services.auth.api.phone)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

android {
    namespace = "com.atharvyadav22.otpverificationkmp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.atharvyadav22.otpverificationkmp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.atharvyadav22.otpverificationkmp"
            packageVersion = "1.0.0"
        }
    }
}

mavenPublishing {
    publishToMavenCentral()
    signAllPublications()

    coordinates(
        groupId = "io.github.atharvyadav22",
        artifactId = "otp-verification-kmp",
        version = "1.0.0"
    )

    pom {
        name.set("OTP Verification KMP")
        description.set("A lightweight, plug-and-play OTP text field for Kotlin Multiplatform.")
        inceptionYear.set("2025")
        url.set("https://github.com/atharvyadav22/otpVerificationKmp")

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("atharvyadav22")
                name.set("Atharv Yadav")
                url.set("https://github.com/atharvyadav22/")
                email.set("dev.atharvyadav@gmail.com")
            }
        }

        scm {
            url.set("https://github.com/atharvyadav22/otpVerificationKmp")
            connection.set("scm:git:git://github.com/atharvyadav22/otpVerificationKmp.git")
            developerConnection.set("scm:git:ssh://git@github.com/atharvyadav22/otpVerificationKmp.git")
        }
    }
}
