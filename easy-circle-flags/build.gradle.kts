plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.sam.easycircleflags"
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Coil for image loading + SVG decoding
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)

    // Compose BOM + essential modules
    implementation(platform(libs.androidx.compose.bom.v20251200))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.foundation)
}