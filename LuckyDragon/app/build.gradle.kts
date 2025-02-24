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
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.firestore)
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.zxing:core:3.3.0")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation(libs.play.services.location)
    androidTestImplementation(libs.junit.jupiter)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.0.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.0.1")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("org.mockito:mockito-core:5.14.2")
    testImplementation("androidx.test.ext:junit:1.2.1")
    testImplementation(libs.junit.jupiter);
    androidTestImplementation("org.mockito:mockito-core:5.14.2")
    androidTestImplementation("org.mockito:mockito-android:5.14.2")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    implementation("org.osmdroid:osmdroid-android:6.1.20")
}