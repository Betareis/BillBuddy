// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.3.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
}


buildscript{
    repositories{
        google()
        mavenCentral()
    }
    dependencies{

        classpath ("com.google.gms:google-services:4.4.1")


    }
}