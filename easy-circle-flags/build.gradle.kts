plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
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

    publishing {
        singleVariant("release") {}
    }
}

dependencies {
    api(libs.coil)
    api(libs.coil.compose)
    api(libs.coil.svg)

    implementation(platform(libs.androidx.compose.bom.v20251200))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.foundation)
}

group = "com.github.sam-a1a"
version = "1.0.0"

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])
                groupId = group.toString()
                artifactId = "easy-circle-flags"
                version = version.toString()
            }
        }
    }
}