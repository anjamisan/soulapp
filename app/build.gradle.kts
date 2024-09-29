plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 34
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.mediarouter)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    val roomVersion = "2.5.2"

    // Room runtime
    implementation("androidx.room:room-runtime:$roomVersion")

    // Annotation processor for Java code
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    implementation("androidx.preference:preference:1.2.0")

    implementation("androidx.recyclerview:recyclerview:1.2.1")

    implementation("androidx.cardview:cardview:1.0.0")

    implementation ("androidx.work:work-runtime-ktx:2.7.1")

    // Retrofit for network calls
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson converter for parsing JSON data
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp for making HTTP requests (Retrofit uses OkHttp by default)
    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // OkHttp logging interceptor (for logging HTTP requests and responses)
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
}