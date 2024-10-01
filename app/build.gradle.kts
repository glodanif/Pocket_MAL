import org.jetbrains.kotlin.config.KotlinCompilerVersion
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.android.gms.oss-licenses-plugin")
    id("com.google.dagger.hilt.android")
    kotlin("android")
}

android {

    namespace = "com.g.pocketmal"

    compileSdkVersion(34)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(34)
        versionCode = 216
        versionName = "5.0.6"
        applicationId = "com.g.pocketmal"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        buildConfig = true
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    signingConfigs {

        create("release") {
            val keystorePropertiesFile = rootProject.file("/pocketmal.properties")
            val keystoreProperties = Properties()
            keystoreProperties.load(FileInputStream(keystorePropertiesFile))
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = File(keystoreProperties["keystore"] as String)
            storePassword = keystoreProperties["keystore.password"] as String
        }
    }

    buildTypes {

        debug {
            buildConfigField("boolean", "SCREENSHOTS", "false")
            val propertiesFile = rootProject.file("pocketmal.properties")
            val properties = Properties()
            properties.load(FileInputStream(propertiesFile))
            buildConfigField("String", "clientId", "\"${properties["clientId"]}\"")
            buildConfigField("String", "redirectUrl", "\"${properties["redirectUrl"]}\"")
        }

        release {
            /*isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )*/
            val releaseSigning = signingConfigs.getByName("release")
            signingConfig = releaseSigning
            buildConfigField("boolean", "SCREENSHOTS", "false")
            val propertiesFile = rootProject.file("pocketmal.properties")
            val properties = Properties()
            properties.load(FileInputStream(propertiesFile))
            buildConfigField("String", "clientId", "\"${properties["clientId"]}\"")
            buildConfigField("String", "redirectUrl", "\"${properties["redirectUrl"]}\"")
        }

        /*screenshots {
             signingConfig signingConfigs.debug
             buildConfigField "boolean", "SCREENSHOTS", "true"

             def properties = getLocalProperties()
             if (properties != null) {
                 buildConfigField 'String', "clientId", "\"${properties['clientId']}\""
                 buildConfigField 'String', "redirectUrl", "\"${properties['redirectUrl']}\""
             } else {
                 buildConfigField 'String', "clientId", "\"\""
                 buildConfigField 'String', "redirectUrl", "\"\""
             }
         }*/
    }

    dependencies {

        implementation(project(":domain"))

        implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
        implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

        val composeBom = platform("androidx.compose:compose-bom:2024.09.00")
        implementation(composeBom)
        androidTestImplementation(composeBom)
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.ui:ui-tooling-preview")
        debugImplementation("androidx.compose.ui:ui-tooling")
        androidTestImplementation("androidx.compose.ui:ui-test-junit4")
        debugImplementation("androidx.compose.ui:ui-test-manifest")
        implementation("androidx.compose.ui:ui-text-google-fonts:1.7.2")
        implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
        implementation("androidx.compose.material:material-icons-extended:1.7.2")

        configurations.all {
            resolutionStrategy {
                force("androidx.compose.compiler:compiler:1.5.15")
            }
        }

        implementation("androidx.navigation:navigation-compose:2.8.1")
        implementation("androidx.activity:activity-compose:1.9.2")

        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.gridlayout:gridlayout:1.0.0")
        implementation("androidx.browser:browser:1.8.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("com.google.android.material:material:1.12.0")

        implementation("com.google.android.play:review:2.0.1")
        implementation("com.google.android.play:review-ktx:2.0.1")

        implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
        implementation("com.google.firebase:firebase-crashlytics")
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-perf")

        implementation("com.squareup.picasso:picasso:2.71828")
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("io.coil-kt:coil-compose:2.7.0")
        implementation("net.engawapg.lib:zoomable:1.6.2")

        implementation("com.google.android.gms:play-services-oss-licenses:17.1.0")

        val activityVersion = "1.9.2"
        implementation("androidx.activity:activity:$activityVersion")
        implementation("androidx.activity:activity-ktx:$activityVersion")
        implementation("androidx.activity:activity-compose:$activityVersion")

        val hilt = "2.51.1"
        implementation("com.google.dagger:hilt-android:$hilt")
        ksp("com.google.dagger:hilt-compiler:$hilt")

        /*androidTestImplementation "com.android.support:support-annotations:$ANDROID_SUPPORT_VERSION"
        androidTestImplementation "com.android.support.test.espresso:espresso-core:$ESPRESSO_VERSION"
        androidTestImplementation("com.android.support.test.espresso:espresso-contrib:$ESPRESSO_VERSION") {
            exclude group: 'com.android.support', module: 'appcompat'
            exclude group: 'com.android.support', module: 'support-v4'
            exclude group: 'com.android.support', module: 'support-v7'
            exclude group: 'com.android.support', module: 'design'
            exclude module: 'support-annotations'
            exclude module: 'recyclerview-v7'
        }
        androidTestImplementation 'androidx.test:runner:1.3.0'
        androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
        androidTestImplementation 'junit:junit:4.13.1'*/
    }
}
