import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "net.mattiascibien.kmm-wifi-connect"

version = "SNAPSHOT"

if (project.hasProperty("ciVersion")) {
    version = project.properties["ciVersion"].toString()
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        dependencies {
            implementation(libs.androidx.startup.runtime)
        }
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "net.mattiascibien.wificonnect"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

//repositories {
//    maven {
//        name = "githubPackages"
//        url = uri("https://maven.pkg.github.com/mattiascibien/kmm-wifi-connect")
//        // username and password (a personal Github access token) should be specified as
//        // `githubPackagesUsername` and `githubPackagesPassword` Gradle properties or alternatively
//        // as `ORG_GRADLE_PROJECT_githubPackagesUsername` and `ORG_GRADLE_PROJECT_githubPackagesPassword`
//        // environment variables
//        credentials(PasswordCredentials::class)
//    }
//}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "library", version.toString())

    pom {
        name = "Kotlin Multiplatform WiFi Connect"
        description = "Library for connecting to WiFi network in a simple and cross-platform way"
        inceptionYear = "2025"
        url = "https://github.com/mattiascibien/kmm-wifi-connect/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "http://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "mattiascibien"
                name = "Mattias Cibien"
                url = "https://github.com/mattiascibien/"
            }
        }
        scm {
            url = "https://github.com/mattiascibien/kmm-wifi-connect/"
            connection = "scm:git:git://github.com/mattiascibien/kmm-wifi-connect.git"
            developerConnection = "scm:git:ssh://git@github.com/mattiascibien/kmm-wifi-connect.git"
        }
    }
}
