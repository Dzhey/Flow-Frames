plugins {
    id("com.android.library")
}

android {
    namespace = "com.github.dzhey.flow_frames"
    compileSdk = 34

    defaultConfig {
        minSdk = 28
    }

    testOptions {
        targetSdk = 34
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.0.0")
    implementation("com.squareup.mortar:mortar:0.20")
    implementation("com.squareup.flow:flow:1.0.0-alpha2")
    implementation("org.parceler:parceler-api:1.1.12")

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.1.0") {
        exclude("com.android.support", "support-annotations")
    }

    annotationProcessor("org.parceler:parceler:1.1.12")
}
