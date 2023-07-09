plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
}

android {
    val androidMinSdk: String by project
    val androidCompileSdk: String by project
    val androidTargetSdk: String by project

    namespace = "com.sh.michael.simple_notepad"
    compileSdk = androidCompileSdk.toInt()

    defaultConfig {
        applicationId = "com.sh.michael.simple_notepad"
        minSdk = androidMinSdk.toInt()
        targetSdk = androidTargetSdk.toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.sh.michael.simple_notepad.TestRunner"
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

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }

    packagingOptions {
        resources.excludes.add("META-INF/*")
    }

    testOptions {
        packagingOptions {
            jniLibs {
                useLegacyPackaging = true
            }
        }

        unitTests.all {
            it.useJUnitPlatform()
        }
    }
}

configurations {
    create("cleanedAnnotations")
    implementation {
        exclude(group = "org.jetbrains", module = "annotations")
    }
}

dependencies {

    implementation(libs.android.core)
    implementation(libs.android.appcompat)
    implementation(libs.android.constraint)
    implementation(libs.android.lifecycle)
    implementation(libs.material)
    implementation(libs.navigation.ui)
    implementation(libs.navigation.fragment)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    implementation(libs.kotlin.coroutines)
    implementation(libs.koin.core)
    implementation(libs.koin.android)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.mockk.core)
    testImplementation(libs.test.coroutines)

    androidTestImplementation(libs.test.junit)
    androidTestImplementation(libs.test.android)
    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.test.runner)
    androidTestImplementation(libs.test.core)
    androidTestImplementation(libs.test.espresso.core)
    androidTestImplementation(libs.test.espresso.contrib)
    androidTestImplementation(libs.test.espresso.idling)
    androidTestImplementation(libs.test.espresso.intents)
    androidTestImplementation(libs.test.fragment)
    androidTestImplementation(libs.test.coroutines)
    androidTestImplementation(libs.test.koin)

//    debugImplementation(libs.test.fragment)
}
