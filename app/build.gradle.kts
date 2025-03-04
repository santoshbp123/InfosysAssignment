plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.infosysassignment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.infosysassignment"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation 'com.google.firebase:firebase-messaging:23.0.0' // Firebase Cloud Messaging
    implementation 'com.google.firebase:firebase-firestore:24.0.0' // Firebase Firestore
    implementation 'com.google.android.gms:play-services-location:18.0.0' //

    implementation 'com.amazonaws:aws-android-sdk-sns:2.16.0'// Location Service
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}