import io.gitlab.arturbosch.detekt.Detekt
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
  id("com.android.application") version "8.3.0"
  id("org.jetbrains.kotlin.android") version "1.9.22"
  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.22"
  id("com.google.devtools.ksp") version "1.9.22-1.0.18"
  id("com.ncorti.ktfmt.gradle") version "0.17.0"
  id("io.gitlab.arturbosch.detekt") version "1.23.5"
  id("com.github.ben-manes.versions") version "0.51.0"
}

android {
  namespace = "app.opass.ccip.compose"
  testNamespace = "app.opass.ccip.compose.test"
  compileSdk = 34

  defaultConfig {
    applicationId = "app.opass.ccip.compose"
    testApplicationId = "app.opass.ccip.compose.test"
    minSdk = 33
    targetSdk = 34
    versionCode = 1
    versionName = "0.1.0"

    vectorDrawables {
      useSupportLibrary = true
    }
  }
  signingConfigs {
    create("release") {
      storeFile = file(System.getenv("KEYSTORE_FILE") ?: "KEYSTORE_FILE")
      storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "KEYSTORE_PASSWORD"
      keyAlias = System.getenv("KEY_ALIAS") ?: "KEY_ALIAS"
      keyPassword = System.getenv("KEY_PASSWORD") ?: "KEY_PASSWORD"

      enableV1Signing = false
      enableV2Signing = false
      enableV3Signing = true
      enableV4Signing = true
    }
  }
  buildTypes {
    debug {
      applicationIdSuffix = ".debug"
      versionNameSuffix = "-debug"
    }
    release {
      isMinifyEnabled = true
      proguardFiles(
          getDefaultProguardFile("proguard-android-optimize.txt"),
          "proguard-rules.pro",
      )
      signingConfig = signingConfigs.getByName("release")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlin {
    jvmToolchain(17)
  }
  buildFeatures {
    compose = true
  }
  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.10"
  }
  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
  testOptions {
    unitTests.all {
      it.useJUnitPlatform()
    }
  }
}

tasks.withType<Detekt>().configureEach {
  reports {
    html.required.set(true)
    xml.required.set(false)
    txt.required.set(false)
    sarif.required.set(true)
    md.required.set(false)
  }
}

detekt {
  config.setFrom("$projectDir/config/detekt.yml")
}

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
  // 1.2.3
  // 1.2.3-4
  // 1.2.3-4.5
  // 1.2.3-4.5.6
  val regex = Regex("""^\d+\.\d+\.\d+(-\d+(\.\d+){0,2})?$""")
  rejectVersionIf { !regex.matches(candidate.version) }
}

dependencies {
  val composeVersion = "1.6.2"
  val ktorVersion = "2.3.8"
  val lifecycleVersion = "2.7.0"

  implementation("androidx.core", "core-ktx", "1.12.0")

  implementation(platform("androidx.compose:compose-bom:2024.02.01"))
  implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.22"))
  implementation("androidx.lifecycle", "lifecycle-runtime-ktx", lifecycleVersion)

  // Jetpack Compose
  implementation("androidx.compose.ui", "ui", composeVersion)
  implementation("androidx.compose.ui", "ui-graphics", composeVersion)
  implementation("androidx.compose.ui", "ui-tooling-preview", composeVersion)
  implementation("androidx.compose.material3", "material3", "1.2.0")
  implementation("androidx.activity", "activity-compose", "1.8.2")
  implementation("androidx.lifecycle", "lifecycle-viewmodel-compose", lifecycleVersion)
  implementation("androidx.lifecycle", "lifecycle-runtime-compose", lifecycleVersion)

  // Navigation
  implementation("io.github.raamcosta.compose-destinations", "core", "1.10.1")
  ksp("io.github.raamcosta.compose-destinations", "ksp", "1.10.1")

  implementation("io.coil-kt", "coil-base", "2.6.0")
  implementation("io.coil-kt", "coil-compose", "2.6.0")

  implementation("io.github.g0dkar", "qrcode-kotlin-android", "4.1.1")


  implementation("org.jetbrains.kotlinx", "kotlinx-datetime", "0.5.0")
  implementation("org.jetbrains.kotlinx", "kotlinx-collections-immutable-jvm", "0.3.7")

  // HTTP Client
  implementation("io.ktor", "ktor-client-core", ktorVersion)
  implementation("io.ktor", "ktor-client-okhttp", ktorVersion)
  implementation("io.ktor", "ktor-client-content-negotiation", ktorVersion)
  implementation("io.ktor", "ktor-serialization-kotlinx-json", ktorVersion)
  implementation("io.ktor", "ktor-client-logging", ktorVersion)
  implementation("org.slf4j", "slf4j-android", "1.7.36")


  // DI
  implementation("io.insert-koin", "koin-android", "3.5.3")
  implementation("io.insert-koin", "koin-androidx-compose", "3.5.3")

  // Linting
  detektPlugins("io.nlopez.compose.rules:detekt:0.3.11")

  // Unit Test
  testImplementation("org.jetbrains.kotlin", "kotlin-reflect", "1.9.22")
  testImplementation("io.kotest", "kotest-runner-junit5", "5.8.0")
  testImplementation("io.ktor", "ktor-client-mock", ktorVersion)

  debugImplementation("androidx.compose.ui", "ui-tooling", composeVersion)
  debugImplementation("androidx.compose.ui", "ui-test-manifest", composeVersion)
}
