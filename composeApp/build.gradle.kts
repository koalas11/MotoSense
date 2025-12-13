import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
	alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.buildkonfig)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.material.icons.extended)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            implementation(libs.androidx.window.core)
            implementation(libs.androidx.savedstate.compose)

            /* SERIALIZATION */
            implementation(libs.serialization.core)
            implementation(libs.serialization.protobuf)

            /* NAVIGATION 3 */
            implementation(libs.androidx.navigation3.runtime)
            implementation(libs.androidx.navigation3.ui)
            implementation(libs.androidx.lifecycle.viewmodel.navigation3)

            /* ROOM */
            implementation(libs.androidx.sqlite.bundled)
            implementation(libs.androidx.room.runtime)

            /* DATASTORE */
            implementation(libs.androidx.datastore.core)

            /* VICO */
            implementation(libs.vico.multiplatform)
            implementation(libs.vico.multiplatform.m3)

            /* DATETIME */
            implementation(libs.kotlinx.datetime)

            /* MAPLIBRE */
            implementation(libs.maplibre.compose)
            implementation(libs.maplibre.compose.m3)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "org.lpss.motosense"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.lpss.motosense"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        debug {
            isDebuggable = true
            isJniDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            resValue("string", "app_name", "MotoSense (Debug)")
            applicationIdSuffix = ".Debug"
        }
        release {
            isDebuggable = false
            isJniDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    debugImplementation(libs.compose.ui.tooling)
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
}

buildkonfig {
    packageName = "org.lpss.motosense"
    objectName = "MotoSenseBuildConfig"

    defaultConfigs {
        val isDebug = gradle.startParameter.taskNames.any { it.contains("Debug", ignoreCase = true) }

        buildConfigField(
            BOOLEAN,
            "DEBUG_MODE",
            isDebug.toString(),
            nullable = false,
            const = true
        )
        val lp = rootProject.file("local.properties")
        val useMockDataFromProp: Boolean =
            if (lp.exists()) {
                Properties().apply { load(lp.inputStream()) }
                    .getProperty("useMockData")?.toBoolean() ?: false
            } else false

        buildConfigField(
            BOOLEAN,
            "USE_MOCK_DATA",
            if (useMockDataFromProp) "true" else "false",
            nullable = false,
            const = true
        )
    }
}
