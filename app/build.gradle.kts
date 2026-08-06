plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.sam.easy_circle_flags"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.sam.easy_circle_flags"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += setOf(
                // Metadata for kotlin-reflect, which is not on the runtime classpath;
                // ~53 KB of files nothing can read.
                "**/*.kotlin_builtins",
                // Compile-time only: the Kotlin compiler reads these, the app never does.
                "**/*.kotlin_metadata",
                "**/*.kotlin_module",
                // Coroutines debug agent index, used only under the debugger.
                "DebugProbesKt.bin",
                // Per-artifact build stamps AndroidX ships; no runtime meaning.
                "META-INF/*.version",
                "META-INF/androidx.*.version",
                "META-INF/com.android.*.version"
            )
            // Note: META-INF/**/LICENSE.txt is another ~51 KB and is commonly excluded
            // too, but that is an attribution decision rather than a build one, so it
            // stays until it is handled somewhere else in the app.
        }
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.core)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(project(":easy-circle-flags"))
}