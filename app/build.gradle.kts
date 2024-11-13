plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.hiltAndroid)


}

android {
    namespace = "com.example.margajaya"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.margajaya"
        minSdk = 27
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.androidx.recyclerview)
    implementation(libs.glide)

    implementation (libs.androidx.navigation.fragment.ktx)
    implementation (libs.androidx.navigation.ui.ktx)

    implementation(libs.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //google maps
    implementation("com.google.android.gms:play-services-maps:19.0.0")
    implementation("com.google.android.gms:play-services-location:21.3.0")


    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation ("androidx.security:security-crypto:1.1.0-alpha06")

    //lottie
    implementation ("com.airbnb.android:lottie:6.4.1")

    implementation ("nl.joery.animatedbottombar:library:1.1.0")

    implementation ("androidx.work:work-runtime-ktx:2.10.0")

    implementation (libs.lifecycle.livedata.ktx)
    implementation (libs.androidx.lifecycle.lifecycle.viewmodel.ktx)
    //slider
 /*   implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")*/
}