plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id ("io.realm.kotlin")
    id("com.google.gms.google-services")
    id ("kotlin-kapt")
    id ("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.expensestracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.expensestracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation ("com.airbnb.android:lottie-compose:6.4.0")
    implementation("androidx.core:core-ktx:1.13.0")
   // implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")
    implementation ("io.realm.kotlin:library-base:1.11.0")
    // Add Realm dependencies

        implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.2")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
        implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.2")

   // implementation ("com.android.support:multidex:2.0.1")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.compose.material3:material3-adaptive:1.0.0-alpha06")
    implementation ("com.marosseleng.android:compose-material3-datetime-pickers:0.6.0")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.0.0-alpha02")
    implementation ("me.saket.swipe:swipe:1.0.0")
   implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation(platform("androidx.compose:compose-bom:2024.01.00"))
    implementation ("com.github.skydoves:colorpicker-compose:1.0.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
   androidTestImplementation(platform("androidx.compose:compose-bom:2024.01.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    implementation ("com.github.tehras:charts:0.2.4-alpha")
    implementation ("com.google.accompanist:accompanist-pager:0.29.1-alpha")
    //implementation ("com.google.accompanist:accompanist-pager:0.29.1-alpha")
   // implementation ("io.sentry:sentry-android:6.13.1")
  //  implementation ("io.sentry:sentry-compose-android:6.13.1")
   // implementation("androidx.lifecycle:lifecycle-view-model-compose:2.6.1")


   // implementation ("androidx.hilt:hilt-compiler:1.2.0")
   // implementation("com.google.dagger:hilt-compiler:2.44")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
   // implementation("androidx.lifecycle:lifecycle-view-model-compose:2.6.1")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("androidx.compose.animation:animation")
  //  implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.8.1-rc")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.4")
   // implementation ("io.github.serpro69:kotlin-faker:1.13.0")
    implementation ("io.github.serpro69:kotlin-faker:1.13.0")
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    //implementation ("com.google.dagger:dagger:2.44")
    implementation ("com.google.dagger:hilt-android:2.44")
    annotationProcessor ("com.google.dagger:hilt-compiler:2.44")
    implementation ("androidx.core:core-splashscreen:1.0.1")





    
}
//hilt {
//    enableAggregatingTask = true
//}