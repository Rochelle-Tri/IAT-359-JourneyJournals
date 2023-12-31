plugins {
    id("com.android.application")
}


android {
    namespace = "com.example.journeyjournals"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.journeyjournals"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.camera:camera-view:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")


//    implementation ("androidx.camera.view:camera-view:1.1.0-alpha02")
//    implementation ("androidx.camera:camera-core:${camerax_version}")
//
//    implementation ("androidx.camera:camera-camera2:${camerax_version}")
//    implementation ("androidx.camera:camera-lifecycle:${camerax_version}")
//    implementation ("androidx.camera:camera-video:${camerax_version}")
//
//    implementation ("androidx.camera:camera-view:${camerax_version}")
//    implementation ("androidx.camera:camera-extensions:${camerax_version}")
    val camerax_version = "1.1.0-beta01"

    implementation ("androidx.camera:camera-core:1.3.0")
    implementation ("androidx.camera:camera-camera2:1.3.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-video:1.3.0")

    implementation ("androidx.camera:camera-view:1.3.0")
    implementation ("androidx.camera:camera-extensions:1.3.0")

    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")
}