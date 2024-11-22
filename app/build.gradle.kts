plugins {
    alias(libs.plugins.android.application)
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")

}

android {
    namespace = "com.example.kamandanoe"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.kamandanoe"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
        testImplementation("org.mockito.kotlin:mockito-kotlin:5.3.1")
        testImplementation("org.mockito:mockito-core:5.7.0")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
        testImplementation("androidx.arch.core:core-testing:2.2.0")
        testImplementation("com.google.truth:truth:1.1.5")
        testImplementation("org.robolectric:robolectric:4.9")
        testImplementation("com.google.dagger:hilt-android-testing:2.51.1")
        androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
        implementation(libs.androidx.recyclerview)
        implementation(libs.glide)

        implementation(libs.androidx.navigation.fragment.ktx)
        implementation(libs.androidx.navigation.ui.ktx)

        implementation(libs.logging.interceptor)
        implementation(libs.retrofit)
        implementation(libs.converter.gson)

        //google maps
        implementation("com.google.android.gms:play-services-maps:19.0.0")
        implementation("com.google.android.gms:play-services-location:21.3.0")

        implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
        implementation("androidx.security:security-crypto:1.1.0-alpha06")

        //lottie
        implementation("com.airbnb.android:lottie:6.4.1")

        implementation("nl.joery.animatedbottombar:library:1.1.0")

        implementation("androidx.work:work-runtime-ktx:2.10.0")

        implementation(libs.lifecycle.livedata.ktx)
        implementation(libs.androidx.lifecycle.lifecycle.viewmodel.ktx)

        implementation ("com.facebook.shimmer:shimmer:0.5.0")
        implementation(libs.androidx.room.runtime)
        ksp(libs.androidx.room.compiler)
        implementation(libs.androidx.room.ktx)

        implementation(libs.hilt.android)
        ksp(libs.hilt.android.compiler)
    }
}
dependencies {
    implementation(libs.material)
}
