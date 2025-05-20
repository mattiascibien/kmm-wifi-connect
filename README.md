# kmm-wifi-connect

![Maven Central Version](https://img.shields.io/maven-central/v/net.mattiascibien/kmm-wifi-connect)


A Kotlin Multiplatform Mobile (KMM) library designed to simplify Wi-Fi connectivity management across common platforms, targeting Android and iOS. This library provides a unified API connecting to a specific Wi-Fi network. The primary use case is for connecting to hotspots exposed by IoT devices.

## Features

* **Cross-Platform Wi-Fi API:** Offers a consistent interface for connecting to Wi-Fi networks in your KMM shared code.
* **Android/iOS Implementation:** Provides a robust implementation, leveraging platform-specific Wi-Fi APIs.
* **Easy Initialization:** On Android it utilizes `androidx.startup` for effortless and efficient initialization on Android.
* **Supports network security:**: Unsecured, WEP (iOS only), WPA2, WPA3

## Installation

Integrate `kmm-wifi-connect` into your KMM project by adding the necessary dependencies to your `build.gradle.kts` files.

### Kotlin Multiplatform Mobile (KMM) Shared Module

In your `composeApp/build.gradle.kts` file (or the `build.gradle.kts` of your KMM shared module), add the dependency to the `commonMain` source set:

```kotlin
// composeApp/build.gradle.kts

kotlin {
    [...]
    sourceSets {
        commonMain.dependencies {
            // Replace 'LATEST_VERSION' with the actual latest version of the library
            implementation("net.mattiascibien:kmm:wifi-connect:LATEST_VERSION") 
        }
        
        androidMain.dependencies {
            // Replace 'LATEST_VERSION' with the actual latest version of the library
            implementation("net.mattiascibien:kmm:wifi-connect-android:LATEST_VERSION") 
            implementation("androidx.startup:startup-runtime:LATEST_VERSION")
        }
    }
}
```

### Android configuration

Open your AndroidManifest.xml and add the following code under the `application` node:

```xml
<provider
    android:name="androidx.startup.InitializationProvider"
    android:authorities="${applicationId}.androidx-startup"
    android:exported="false"
    tools:node="merge">
    <meta-data  android:name="net.mattiascibien.wificonnect.AndroidServicesInitializer"
                android:value="androidx.startup" />
</provider>
```

## API

The following method can be used to connect to the specified Wi-Fi network.

```kotlin
expect suspend fun connectToWifi(ssid: String, type: WiFiType, password: String? = null) : Boolean
```
