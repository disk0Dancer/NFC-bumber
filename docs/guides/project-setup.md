# Project Setup Guide - NFC Card Emulator

## Prerequisites

### Required Software

1. **Android Studio**
   - Version: Hedgehog (2023.1.1) or later
   - Download: https://developer.android.com/studio
   - Plugins: Kotlin, Gradle

2. **Java Development Kit (JDK)**
   - Version: JDK 17 (recommended)
   - Download: https://adoptium.net/

3. **Android SDK**
   - Minimum SDK: API 26 (Android 8.0)
   - Target SDK: API 34 (Android 14)
   - Build Tools: 34.0.0

4. **Gradle**
   - Version: 8.2+ (included with Android Studio)
   - Gradle Wrapper: Use included wrapper

5. **Git**
   - Version: 2.30+
   - Download: https://git-scm.com/

### Optional Tools

- **Android Device with NFC** (for testing)
  - Android 8.0 or higher
  - NFC hardware required
  - HCE support required

- **Physical NFC Cards** (for testing)
  - ISO 14443-4 compatible cards
  - MIFARE Classic/Ultralight cards
  - Access cards, transit cards, etc.

---

## Initial Setup

### 1. Clone the Repository

```bash
# Using HTTPS
git clone https://github.com/disk0Dancer/NFC-bumber.git

# Using SSH
git clone git@github.com:disk0Dancer/NFC-bumber.git

# Navigate to project directory
cd NFC-bumber
```

### 2. Configure Android Studio

1. **Open Android Studio**
2. **File > Open** and select the cloned repository
3. Wait for Gradle sync to complete
4. If prompted, accept SDK licenses:

```bash
# From terminal/command prompt
cd path/to/android-sdk
tools/bin/sdkmanager --licenses
```

### 3. Configure JDK

1. **File > Project Structure > SDK Location**
2. Set **JDK Location** to JDK 17
3. Click **Apply** and **OK**

### 4. Configure Gradle

The project uses Gradle Wrapper, no manual configuration needed. If issues occur:

```bash
# From project root
./gradlew clean build

# On Windows
gradlew.bat clean build
```

### 5. Verify Setup

Run the following commands to verify setup:

```bash
# Check Gradle version
./gradlew --version

# Check project structure
./gradlew projects

# Run tests (when available)
./gradlew test

# Build debug APK
./gradlew assembleDebug
```

---

## Project Structure

```
NFC-bumber/
├── .github/                    # GitHub Actions workflows
├── app/                        # Main application module
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/           # Kotlin source code
│   │   │   │   └── com/
│   │   │   │       └── nfcbumber/
│   │   │   │           ├── data/           # Data layer
│   │   │   │           │   ├── database/   # Room database
│   │   │   │           │   ├── model/      # Data models
│   │   │   │           │   └── repository/ # Repositories
│   │   │   │           ├── domain/         # Business logic
│   │   │   │           │   ├── usecase/    # Use cases
│   │   │   │           │   └── model/      # Domain models
│   │   │   │           ├── presentation/   # UI layer
│   │   │   │           │   ├── main/       # Main screen
│   │   │   │           │   ├── scan/       # Scan screen
│   │   │   │           │   ├── details/    # Details screen
│   │   │   │           │   ├── settings/   # Settings screen
│   │   │   │           │   └── common/     # Shared UI components
│   │   │   │           ├── service/        # Android services
│   │   │   │           │   ├── nfc/        # NFC reader service
│   │   │   │           │   └── hce/        # HCE emulator service
│   │   │   │           ├── di/             # Dependency injection
│   │   │   │           └── util/           # Utilities
│   │   │   ├── res/            # Resources
│   │   │   │   ├── layout/     # XML layouts (if not using Compose)
│   │   │   │   ├── values/     # Strings, colors, themes
│   │   │   │   ├── drawable/   # Images, icons
│   │   │   │   └── xml/        # HCE service config
│   │   │   └── AndroidManifest.xml
│   │   ├── test/               # Unit tests
│   │   └── androidTest/        # Instrumentation tests
│   └── build.gradle.kts        # App module build config
├── docs/                       # Documentation
│   ├── architecture/           # Architecture docs
│   ├── requirements/           # Requirements docs
│   └── guides/                 # Setup and style guides
├── gradle/                     # Gradle wrapper
├── .gitignore                  # Git ignore rules
├── build.gradle.kts            # Project-level build config
├── settings.gradle.kts         # Gradle settings
├── gradle.properties           # Gradle properties
├── LICENSE                     # BSD 3-Clause License
└── README.md                   # Project README
```

---

## Build Configuration

### Gradle Configuration (build.gradle.kts)

**Project-level** (`build.gradle.kts`):

```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.20" apply false
    id("com.google.dagger.hilt.android") version "2.48" apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
```

**App-level** (`app/build.gradle.kts`):

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.nfcbumber"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nfcbumber"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-Xcontext-receivers"
        )
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Core Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-ktx:1.8.2")
    
    // Compose
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    
    // Dependency Injection
    implementation("com.google.dagger:hilt-android:2.48")
    ksp("com.google.dagger:hilt-compiler:2.48")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    
    // Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    
    // Security
    implementation("androidx.security:security-crypto:1.1.0-alpha06")
    implementation("androidx.biometric:biometric:1.2.0-alpha05")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    
    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

### Gradle Properties (gradle.properties)

```properties
# Project-wide Gradle settings
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
org.gradle.parallel=true
org.gradle.caching=true

# Kotlin
kotlin.code.style=official

# Android
android.useAndroidX=true
android.nonTransitiveRClass=true

# Disable unnecessary features
android.enableJetifier=false
```

---

## Development Workflow

### 1. Branch Strategy

```bash
# Main branches
main          # Production-ready code
develop       # Development branch

# Feature branches
feature/card-scanning
feature/hce-emulation
feature/ui-slider

# Bug fix branches
bugfix/scan-timeout
bugfix/crash-on-delete

# Release branches
release/1.0.0
release/1.1.0
```

### 2. Commit Convention

Follow [Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add card scanning functionality
fix: resolve NFC timeout issue
docs: update setup guide
style: format code with ktlint
refactor: restructure data layer
test: add unit tests for CardRepository
chore: update dependencies
perf: optimize database queries
```

### 3. Pull Request Process

1. Create feature branch from `develop`
2. Implement changes with tests
3. Run linters and tests locally
4. Push branch and create PR
5. Request code review
6. Address review comments
7. Merge to `develop` after approval

---

## Code Quality Tools

### 1. ktlint (Code Formatting)

**Setup**: Add to `app/build.gradle.kts`

```kotlin
// Plugin
plugins {
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
}

// Configuration
ktlint {
    android.set(true)
    ignoreFailures.set(false)
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}
```

**Usage**:

```bash
# Check code style
./gradlew ktlintCheck

# Auto-format code
./gradlew ktlintFormat
```

### 2. Detekt (Static Analysis)

**Setup**: Add to project `build.gradle.kts`

```kotlin
plugins {
    id("io.gitlab.arturbosch.detekt") version "1.23.4"
}

detekt {
    toolVersion = "1.23.4"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}
```

**Usage**:

```bash
# Run static analysis
./gradlew detekt

# Generate report
./gradlew detektBaseline
```

### 3. JaCoCo (Code Coverage)

**Setup**: Add to `app/build.gradle.kts`

```kotlin
plugins {
    id("jacoco")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
    
    // Specify coverage sources
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(files("build/tmp/kotlin-classes/debug"))
    executionData.setFrom(files("build/jacoco/testDebugUnitTest.exec"))
}
```

**Usage**:

```bash
# Run tests with coverage
./gradlew testDebugUnitTest jacocoTestReport

# View report
open app/build/reports/jacoco/jacocoTestReport/html/index.html
```

---

## Running the Application

### Debug Build

```bash
# Build debug APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug

# Build and run
./gradlew installDebug
# Then launch from device
```

### Release Build

```bash
# Build release APK (requires signing config)
./gradlew assembleRelease

# Build Android App Bundle (for Play Store)
./gradlew bundleRelease
```

### Signing Configuration

Create `keystore.properties` in project root (DO NOT commit):

```properties
storeFile=/path/to/keystore.jks
storePassword=your_store_password
keyAlias=your_key_alias
keyPassword=your_key_password
```

Update `app/build.gradle.kts`:

```kotlin
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
```

---

## Testing

### Unit Tests

```bash
# Run all unit tests
./gradlew test

# Run specific test class
./gradlew test --tests CardRepositoryTest

# Run with coverage
./gradlew testDebugUnitTest jacocoTestReport
```

### Instrumentation Tests

```bash
# Run on connected device
./gradlew connectedAndroidTest

# Run specific test
./gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=com.nfcbumber.MainActivityTest
```

### Test Structure

```
src/
├── test/                    # Unit tests (JVM)
│   └── java/
│       └── com/
│           └── nfcbumber/
│               ├── data/
│               ├── domain/
│               └── presentation/
└── androidTest/             # Instrumentation tests (Android)
    └── java/
        └── com/
            └── nfcbumber/
                ├── ui/
                └── service/
```

---

## Continuous Integration

### GitHub Actions

Create `.github/workflows/android.yml`:

```yaml
name: Android CI

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main, develop ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 17
      uses: actions/setup-java@v3
      with:
        java-version: '17'
        distribution: 'adopt'
    
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    
    - name: Run ktlint
      run: ./gradlew ktlintCheck
    
    - name: Run Detekt
      run: ./gradlew detekt
    
    - name: Run unit tests
      run: ./gradlew test
    
    - name: Build debug APK
      run: ./gradlew assembleDebug
    
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

---

## Troubleshooting

### Common Issues

**Issue**: Gradle sync failed
```bash
# Solution
./gradlew clean
./gradlew build --refresh-dependencies
```

**Issue**: NFC not working in emulator
```
Solution: NFC requires physical device for testing.
Use real Android device with NFC hardware.
```

**Issue**: Build tools not found
```bash
# Solution: Install from Android Studio
Tools > SDK Manager > SDK Tools > Android SDK Build-Tools
```

**Issue**: Out of memory during build
```bash
# Solution: Increase heap size in gradle.properties
org.gradle.jvmargs=-Xmx4096m
```

---

## Additional Resources

- [Android Developer Documentation](https://developer.android.com)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Jetpack Compose Tutorial](https://developer.android.com/jetpack/compose/tutorial)
- [Material Design 3](https://m3.material.io/)
- [NFC Developer Guide](https://developer.android.com/guide/topics/connectivity/nfc)
- [Host Card Emulation](https://developer.android.com/guide/topics/connectivity/nfc/hce)

---

## Getting Help

- **Issues**: [GitHub Issues](https://github.com/disk0Dancer/NFC-bumber/issues)
- **Discussions**: [GitHub Discussions](https://github.com/disk0Dancer/NFC-bumber/discussions)
- **Documentation**: `/docs` folder in repository
- **Code Examples**: `/samples` folder (when available)

---

## Next Steps

After setup, refer to:

1. [Coding Style Guide](./coding-style-guide.md) - Code conventions
2. [Architecture Documentation](../architecture/c4-model.md) - System design
3. [Functional Requirements](../requirements/functional-requirements.md) - Features
4. [Contributing Guide](../../CONTRIBUTING.md) - Contribution guidelines (when available)
