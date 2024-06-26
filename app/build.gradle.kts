plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.engineerfred.kotlin.todoapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.engineerfred.kotlin.todoapp"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
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
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    val hiltVersion = "2.48.1"
    val composeNavigationVersion = "2.7.6"
    val hiltComposeNavigationVersion = "1.1.0"
    val coroutinesVersion = "1.7.3"
    val roomVersion = "2.6.1"
    val workerVersion = "2.8.1"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //firebase real time database
    implementation("com.google.firebase:firebase-database:20.3.0")

    //accompanist library
    implementation("com.google.accompanist:accompanist-permissions:0.35.0-alpha")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.35.0-alpha")

    // Kotlin + coroutines -workerManager
    implementation("androidx.work:work-runtime-ktx:$workerVersion")
    implementation("androidx.hilt:hilt-common:1.1.0")

    //dagger-hilt
    implementation("com.google.dagger:hilt-android:${hiltVersion}")
    implementation("androidx.hilt:hilt-navigation-compose:${hiltComposeNavigationVersion}")
    kapt("com.google.dagger:hilt-android-compiler:${hiltVersion}")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    //view model
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")

    //navigation
    implementation("androidx.navigation:navigation-compose:${composeNavigationVersion}")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${coroutinesVersion}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${coroutinesVersion}")

    //splashScreen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    //coil
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("io.coil-kt:coil-video:2.4.0")

    //more material icons
    implementation("androidx.compose.material:material-icons-extended")

    //constraintlayout
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    //room
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    //dataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("androidx.wear.compose:compose-material:1.3.0")
}

kapt {
    correctErrorTypes = true
}