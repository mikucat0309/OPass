import io.gitlab.arturbosch.detekt.Detekt

plugins {
  id("com.android.application") version "8.2.2"
  id("org.jetbrains.kotlin.android") version "1.9.20"
  id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
  id("com.google.devtools.ksp") version "1.9.20-1.0.14"
  id("com.ncorti.ktfmt.gradle") version "0.15.1"
  id("io.gitlab.arturbosch.detekt") version "1.23.4"
//  id("com.google.protobuf") version "0.9.4"
}

android {
  namespace = "app.opass.ccip.compose"
  testNamespace = "app.opass.ccip.compose"
  compileSdk = 34

  defaultConfig {
    applicationId = "app.opass.ccip.compose"
    testApplicationId = "app.opass.ccip.compose"
    minSdk = 33
    targetSdk = 34
    versionCode = 1
    versionName = "0.1.0"

    vectorDrawables {
      useSupportLibrary = true
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
      signingConfig = signingConfigs.getByName("debug")
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
    kotlinCompilerExtensionVersion = "1.5.5"
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

//protobuf {
//  generateProtoTasks {
//    all().forEach {
//      it.builtins {
//        create("java") {
//          option("lite")
//        }
//        create("kotlin") {
//          option("lite")
//        }
//      }
//    }
//  }
//}

dependencies {
  val composeVersion = "1.5.4"
  val ktorVersion = "2.3.6"

  implementation("androidx.core", "core-ktx", "1.12.0")
  implementation("androidx.lifecycle", "lifecycle-runtime-ktx", "2.6.2")

  implementation(platform("androidx.compose:compose-bom:2023.10.01"))
  implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.9.20"))

  // Jetpack Compose
  implementation("androidx.compose.ui", "ui", composeVersion)
  implementation("androidx.compose.ui", "ui-graphics", composeVersion)
  implementation("androidx.compose.ui", "ui-tooling-preview", composeVersion)
//  implementation("androidx.compose.material", "material-icons-extended", composeVersion)
  implementation("androidx.compose.material3", "material3", "1.1.2")
  implementation("androidx.activity", "activity-compose", "1.8.1")
  implementation("androidx.lifecycle", "lifecycle-viewmodel-compose", "2.6.2")
  implementation("androidx.lifecycle", "lifecycle-runtime-compose", "2.6.2")

  // Navigation
  implementation("io.github.raamcosta.compose-destinations", "core", "1.9.55")
  ksp("io.github.raamcosta.compose-destinations", "ksp", "1.9.55")

  implementation("io.coil-kt", "coil-base", "2.5.0")
  implementation("io.coil-kt", "coil-compose", "2.5.0")

  implementation("io.github.g0dkar", "qrcode-kotlin-android", "4.0.7")


  implementation("org.jetbrains.kotlinx", "kotlinx-datetime", "0.5.0")
  implementation("org.jetbrains.kotlinx", "kotlinx-collections-immutable-jvm", "0.3.7")

  // HTTP Client
  implementation("io.ktor", "ktor-client-core", ktorVersion)
  implementation("io.ktor", "ktor-client-okhttp", ktorVersion)
  implementation("io.ktor", "ktor-client-content-negotiation", ktorVersion)
  implementation("io.ktor", "ktor-serialization-kotlinx-json", ktorVersion)
  implementation("io.ktor", "ktor-client-logging", ktorVersion)

  // Logging
//  implementation("org.slf4j", "slf4j-android", "1.7.36")
  implementation("uk.uuid.slf4j", "slf4j-android", "2.0.7-0")

  // DI
  implementation("io.insert-koin", "koin-android", "3.5.0")
  implementation("io.insert-koin", "koin-androidx-compose", "3.5.0")


  // Local Cache & Settings
//  implementation("androidx.datastore", "datastore", "1.0.0")
//  implementation("com.google.protobuf", "protobuf-kotlin", "3.25.1")

  // Linting
  detektPlugins("io.nlopez.compose.rules:detekt:0.3.8")

  // Unit Test
  testImplementation("org.jetbrains.kotlin", "kotlin-reflect", "1.9.20")
  testImplementation("io.kotest", "kotest-runner-junit5", "5.8.0")
  testImplementation("io.ktor", "ktor-client-mock", ktorVersion)

  debugImplementation("androidx.compose.ui", "ui-tooling", composeVersion)
  debugImplementation("androidx.compose.ui", "ui-test-manifest", composeVersion)
}
