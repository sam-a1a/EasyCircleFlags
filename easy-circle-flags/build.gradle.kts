// build.gradle.kts
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    id("maven-publish")
}

android {
    namespace = "com.sam.easycircleflags"

    // Held one release behind :app on purpose. This module is what JitPack builds, and
    // an SDK the build image does not have is a broken publish rather than a warning.
    // Lint's GradleDependency notice about 37 being available is expected here.
    //noinspection GradleDependency
    compileSdk = 36

    defaultConfig {
        minSdk = 21
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    // Published to JitPack, so the AAR is compiled against the lowest bytecode
    // level we can get away with: anything newer would force every consumer
    // onto a matching (or newer) JDK just to read our class files.
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
    api(libs.coil.network)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
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