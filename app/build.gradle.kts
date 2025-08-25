plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    //navigation component Apply the Safe Args plugin
    alias(libs.plugins.androidx.navigation.safe.args.kotlin)
    //ksp
    alias(libs.plugins.ksp)

}

android {
    namespace = "com.example.chand"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.chand"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit + Gson + OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp.client)
    implementation(libs.gson)

    // Coil
    implementation(libs.coil.core)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Room
    implementation(libs.room)            // room-ktx
    implementation(libs.room.runtime)    // room-runtime
    ksp(libs.room.compiler)              // room-compiler

    // Work Manager
    implementation (libs.androidx.work.runtime.ktx)

}
