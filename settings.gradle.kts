pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            // Plugin versions
            version("agp", "8.6.0")
            version("kotlin", "2.0.0")
            version("ksp", "2.0.0-1.0.24")
            version("hilt", "2.51.1")

            // Library versions
            version("compose-bom", "2024.10.00")
            version("compose-compiler", "1.5.15")
            version("core-ktx", "1.15.0")
            version("lifecycle", "2.8.7")
            version("activity-compose", "1.9.3")
            version("navigation-compose", "2.8.4")
            version("room", "2.6.1")
            version("coroutines", "1.9.0")
            version("hilt-navigation-compose", "1.2.0")
            version("datastore", "1.1.1")
            version("biometric", "1.2.0-alpha05")
            version("accompanist", "0.36.0")
            version("security-crypto", "1.1.0-alpha06")
            
            // Test versions
            version("junit", "5.11.3")
            version("junit4", "4.13.2")
            version("mockk", "1.13.13")
            version("espresso", "3.6.1")
            version("androidx-test", "1.6.1")
            version("compose-test", "1.7.5")

            // Plugins
            plugin("android-application", "com.android.application").versionRef("agp")
            plugin("android-library", "com.android.library").versionRef("agp")
            plugin("kotlin-android", "org.jetbrains.kotlin.android").versionRef("kotlin")
            plugin("kotlin-kapt", "org.jetbrains.kotlin.kapt").versionRef("kotlin")
            plugin("ksp", "com.google.devtools.ksp").versionRef("ksp")
            plugin("hilt", "com.google.dagger.hilt.android").versionRef("hilt")

            // Core Android
            library("core-ktx", "androidx.core", "core-ktx").versionRef("core-ktx")
            library("lifecycle-runtime-ktx", "androidx.lifecycle", "lifecycle-runtime-ktx").versionRef("lifecycle")
            library("lifecycle-viewmodel-ktx", "androidx.lifecycle", "lifecycle-viewmodel-ktx").versionRef("lifecycle")
            library("lifecycle-viewmodel-compose", "androidx.lifecycle", "lifecycle-viewmodel-compose").versionRef("lifecycle")
            library("lifecycle-runtime-compose", "androidx.lifecycle", "lifecycle-runtime-compose").versionRef("lifecycle")

            // Compose
            library("compose-bom", "androidx.compose", "compose-bom").versionRef("compose-bom")
            library("compose-ui", "androidx.compose.ui", "ui").withoutVersion()
            library("compose-ui-graphics", "androidx.compose.ui", "ui-graphics").withoutVersion()
            library("compose-ui-tooling-preview", "androidx.compose.ui", "ui-tooling-preview").withoutVersion()
            library("compose-ui-tooling", "androidx.compose.ui", "ui-tooling").withoutVersion()
            library("compose-material3", "androidx.compose.material3", "material3").withoutVersion()
            library("activity-compose", "androidx.activity", "activity-compose").versionRef("activity-compose")
            library("navigation-compose", "androidx.navigation", "navigation-compose").versionRef("navigation-compose")

            // Hilt
            library("hilt-android", "com.google.dagger", "hilt-android").versionRef("hilt")
            library("hilt-compiler", "com.google.dagger", "hilt-compiler").versionRef("hilt")
            library("hilt-navigation-compose", "androidx.hilt", "hilt-navigation-compose").versionRef("hilt-navigation-compose")

            // Room
            library("room-runtime", "androidx.room", "room-runtime").versionRef("room")
            library("room-ktx", "androidx.room", "room-ktx").versionRef("room")
            library("room-compiler", "androidx.room", "room-compiler").versionRef("room")

            // Coroutines
            library("coroutines-core", "org.jetbrains.kotlinx", "kotlinx-coroutines-core").versionRef("coroutines")
            library("coroutines-android", "org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef("coroutines")

            // DataStore
            library("datastore-preferences", "androidx.datastore", "datastore-preferences").versionRef("datastore")

            // Biometric
            library("biometric", "androidx.biometric", "biometric").versionRef("biometric")

            // Accompanist
            library("accompanist-permissions", "com.google.accompanist", "accompanist-permissions").versionRef("accompanist")

            // Security
            library("security-crypto", "androidx.security", "security-crypto").versionRef("security-crypto")

            // Testing
            library("junit-jupiter-api", "org.junit.jupiter", "junit-jupiter-api").versionRef("junit")
            library("junit-jupiter-engine", "org.junit.jupiter", "junit-jupiter-engine").versionRef("junit")
            library("junit-jupiter-params", "org.junit.jupiter", "junit-jupiter-params").versionRef("junit")
            library("junit", "junit", "junit").versionRef("junit4")
            library("mockk", "io.mockk", "mockk").versionRef("mockk")
            library("mockk-android", "io.mockk", "mockk-android").versionRef("mockk")
            library("androidx-test-runner", "androidx.test", "runner").versionRef("androidx-test")
            library("androidx-test-rules", "androidx.test", "rules").versionRef("androidx-test")
            library("espresso-core", "androidx.test.espresso", "espresso-core").versionRef("espresso")
            library("compose-ui-test-junit4", "androidx.compose.ui", "ui-test-junit4").versionRef("compose-test")
            library("compose-ui-test-manifest", "androidx.compose.ui", "ui-test-manifest").versionRef("compose-test")
            library("coroutines-test", "org.jetbrains.kotlinx", "kotlinx-coroutines-test").versionRef("coroutines")

            // Bundles
            bundle("compose", listOf("compose-ui", "compose-ui-graphics", "compose-ui-tooling-preview", "compose-material3"))
            bundle("lifecycle", listOf("lifecycle-runtime-ktx", "lifecycle-viewmodel-ktx", "lifecycle-viewmodel-compose", "lifecycle-runtime-compose"))
            bundle("room", listOf("room-runtime", "room-ktx"))
            bundle("coroutines", listOf("coroutines-core", "coroutines-android"))
        }
    }
}

rootProject.name = "NFC-bumber"
include(":app")
include(":data")
include(":domain")
include(":presentation")
