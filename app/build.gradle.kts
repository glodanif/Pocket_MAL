import org.jetbrains.kotlin.config.KotlinCompilerVersion
import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.android.gms.oss-licenses-plugin")
    kotlin("android")
}

android {

    namespace = "com.g.pocketmal"

    compileSdkVersion(34)

    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(34)
        versionCode = 215
        versionName = "5.0.5"
        applicationId = "com.g.pocketmal"
        multiDexEnabled = true
        vectorDrawables.useSupportLibrary = true
        //testInstrumentationRunner("androidx.test.runner.AndroidJUnitRunner")
        /*kapt {
            arguments {
                arg('room.schemaLocation', "$projectDir/schemas".toString())
            }
        }*/
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
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
        implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
        implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))

        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("androidx.recyclerview:recyclerview:1.3.2")
        implementation("androidx.cardview:cardview:1.0.0")
        implementation("androidx.gridlayout:gridlayout:1.0.0")
        implementation("androidx.browser:browser:1.8.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")
        implementation("com.google.android.material:material:1.12.0")

        implementation("com.android.support:customtabs:28.0.0")

        implementation(platform("com.google.firebase:firebase-bom:33.2.0"))
        implementation("com.google.firebase:firebase-crashlytics")
        implementation("com.google.firebase:firebase-analytics")
        implementation("com.google.firebase:firebase-perf")

        implementation("com.github.chrisbanes:PhotoView:2.3.0")
        implementation("io.github.ShawnLin013:number-picker:2.4.12")

        implementation("io.insert-koin:koin-android:2.2.2")

        implementation("com.squareup.okhttp:okhttp:2.7.5")
        implementation("com.squareup.okhttp:okhttp-urlconnection:2.7.5")
        implementation("com.squareup.picasso:picasso:2.71828")
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")
        implementation("com.squareup.retrofit2:converter-scalars:2.9.0")
        implementation("com.squareup.okhttp3:okhttp:4.8.0")
        implementation("com.squareup.okhttp3:logging-interceptor:4.8.0")
        implementation("com.google.code.gson:gson:2.8.6")
        //implementation("com.google.guava:guava-collections:r03")

        implementation("com.google.android.gms:play-services-oss-licenses:17.0.0")

        implementation("at.favre.lib:armadillo:1.0.0")

        implementation("androidx.room:room-runtime:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")
        ksp("androidx.room:room-compiler:2.6.1")

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

        implementation("androidx.core:core-ktx:1.3.2")
    }
}
