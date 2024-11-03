plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.luckydragon"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.luckydragon"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        animationsDisabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    testOptions {
        packaging {
            resources.excludes.add("META-INF/*")
        }
    }

    tasks.withType<Test>{
        useJUnitPlatform()
    }

}

dependencies {
//    implementation(libs.material.v130)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    //noinspection UseTomlInstead
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.zxing:core:3.3.0")
    androidTestImplementation(libs.junit.jupiter)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")
    // for QR Code Scanner
    implementation("com.journeyapps:zxing-android-embedded:4.1.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.1")
}