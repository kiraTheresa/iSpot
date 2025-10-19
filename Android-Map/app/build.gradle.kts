
// 模块级 gradle 配置
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.zjgsu.kiratheresa.iblog"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.zjgsu.kiratheresa.iblog"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
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
        kotlinCompilerExtensionVersion = "1.5.14"
    }

}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    // 高德地图整合包
    //    implementation("com.amap.api:3dmap:latest.integration")
    implementation("com.amap.api:3dmap:9.8.3")

    // Jetpack Compose BOM（版本统一管理）
    implementation(platform("androidx.compose:compose-bom:2024.10.00"))

    // 基础 UI 核心
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")

    // Material 3
    implementation("androidx.compose.material3:material3")

    // Lifecycle (可选但推荐)
    implementation("androidx.lifecycle:lifecycle-runtime-compose")

    // Room 数据库
    implementation("androidx.room:room-runtime:2.8.2")
    implementation("androidx.room:room-ktx:2.8.2")
    kapt("androidx.room:room-compiler:2.8.2")

    // 协程
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.10.2")

    // 权限管理
    implementation("com.guolindev.permissionx:permissionx:1.6.4")



}