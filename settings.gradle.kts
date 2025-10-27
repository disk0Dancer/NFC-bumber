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
            version("agp", "7.4.2")
            version("kotlin", "1.9.0")
            version("ksp", "1.9.0-1.0.13")
            version("hilt", "2.48")

            // Library versions
            version("compose-bom", "2023.10.01")
            version("compose-compiler", "1.5.1")
            version("core-ktx", "1.12.0")
            version("lifecycle", "2.6.2")
            version("activity-compose", "1.8.0")
            version("navigation-compose", "2.7.5")
            version("room", "2.6.0")
            version("coroutines", "1.7.3")
            version("hilt-navigation-compose", "1.1.0")
            version("datastore", "1.0.0")
            version("biometric", "1.1.0")
            version("accompanist", "0.32.0")
            
            // Test versions
            version("junit", "5.10.1")
            version("junit4", "4.13.2")
            version("mockk", "1.13.8")
            version("espresso", "3.5.1")
            version("androidx-test", "1.5.0")
            version("compose-test", "1.5.4")

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
            bundle("lifecycle", listOf("lifecycle-runtime-ktx", "lifecycle-viewmodel-ktx", "lifecycle-viewmodel-compose"))
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
