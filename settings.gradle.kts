pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            val navigationRef = version("navigation", "2.4.1")
            val materialRef = version("material", "1.8.0")
            val androidCompatRef = version("appcompat", "1.6.1")
            val androidConstraintRef = version("constraint", "2.1.4")
            val androidCoreRef = version("core", "1.7.0")
            val androidLifecycleRef = version("lifecycle", "2.6.1")
            val coroutinesRef = version("coroutines", "1.6.2")
            val roomRef = version("room", "2.5.1")
            val koinRef = version("koin", "3.4.0")
            val junitRef = version("junit", "4.13.2")
            val espressoRef = version("espresso", "3.5.1")
            val androidTestRef = version("android-test", "1.1.5")

            alias("navigation-fragment").to("androidx.navigation", "navigation-fragment-ktx").versionRef(navigationRef)
            alias("navigation-ui").to("androidx.navigation", "navigation-ui-ktx").versionRef(navigationRef)
            alias("material").to("com.google.android.material", "material").versionRef(materialRef)
            alias("android-appcompat").to("androidx.appcompat", "appcompat").versionRef(androidCompatRef)
            alias("android-constraint").to("androidx.constraintlayout", "constraintlayout").versionRef(androidConstraintRef)
            alias("android-lifecycle").to("androidx.lifecycle", "lifecycle-livedata-ktx").versionRef(androidLifecycleRef)
            alias("android-core").to("androidx.core", "core-ktx").versionRef(androidCoreRef)
            alias("kotlin-coroutines").to("org.jetbrains.kotlinx", "kotlinx-coroutines-android").versionRef(coroutinesRef)
            alias("koin-core").to("io.insert-koin", "koin-core").versionRef(koinRef)
            alias("koin-android").to("io.insert-koin", "koin-android").versionRef(koinRef)
            alias("room-runtime").to("androidx.room", "room-runtime").versionRef(roomRef)
            alias("room-compiler").to("androidx.room", "room-compiler").versionRef(roomRef)
            alias("room-ktx").to("androidx.room", "room-ktx").versionRef(roomRef)
            alias("test-junit").to("junit", "junit").versionRef(junitRef)
            alias("test-koin").to("io.insert-koin", "koin-test").versionRef(koinRef)
            alias("test-espresso").to("androidx.test.espresso", "espresso-core").versionRef(espressoRef)
            alias("test-android").to("androidx.test.ext", "junit").versionRef(androidTestRef)
        }
    }
}

rootProject.name = "simple-notepad"
include(":app")
