// project-level build.gradle
plugins {
    id("com.android.application") version "8.3.1" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false // <-- CHANGE THIS TO MATCH kotlin-stdlib
    id("com.google.gms.google-services") version "4.4.0" apply false
//    id ("io.realm.kotlin") version "1.11.0" apply false
//    id ("com.google.dagger.hilt.android") version "2.44" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false
}