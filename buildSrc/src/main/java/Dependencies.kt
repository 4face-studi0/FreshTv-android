@file:Suppress("MayBeConstant")

import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.ScriptHandlerScope
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.maven

val repos: RepositoryHandler.() -> Unit get() = {
    google()
    jcenter()
    mavenCentral()
    maven("https://kotlin.bintray.com/kotlinx" )
    maven("https://maven.fabric.io/public" )
    // mavenLocal()
}

val ScriptHandlerScope.classpathDependencies: DependencyHandlerScope.() -> Unit get() = {
    classpath( kotlin("gradle-plugin", Versions.kotlin) )
    classpath("org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}" )
    classpath("com.android.tools.build:gradle:${Versions.android_gradle_plugin}" )
    classpath("com.google.gms:google-services:${Versions.google_gms_services}" )
    classpath("io.fabric.tools:gradle:${Versions.fabric}")
    classpath("com.squareup.sqldelight:gradle-plugin:${Versions.sqldelight}" )
    classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.android_navigation}" )
}

fun DependencyHandler.applyTests() = Libs.run {
    listOf( test, test_junit, mockk )
            .forEach { add("testImplementation", it ) }
}

fun DependencyHandler.applyAndroidTests() {
    Libs.Android.run {
        listOf( robolectric )
            .forEach { add( "testImplementation", it ) }
    }
    Libs.run {
        listOf( test, test_junit, mockk_android )
            .forEach { add("androidTestImplementation", it ) }
    }
    Libs.Android.run {
        listOf( espresso, test_core, test_fragment, test_rules, test_runner )
            .forEach { add( "androidTestImplementation", it ) }
    }
}

object Versions {
    val kotlin =                        "1.3.31"        // Updated: Apr 25, 2019
    val coroutines =                    "1.2.1"         // Updated: Apr 26, 2019
    val serialization =                 "0.11.0"        // Updated: Apr 25, 2019

    val fabric =                        "1.29.0"        // Updated:
    val firebase_core_android =         "16.0.9"        // Updated:
    val firebase_crashlytics_android =  "2.10.1"        // Updated:
    val google_gms_services =           "4.2.0"         // Updated:
    val koin =                          "2.0.1"         // Updated:
    val ktor =                          "1.2.1"         // Updated:
    val mockk =                         "1.9.3"         // Updated:
    val sqldelight =                    "1.1.3"         // Updated:
    val threeten_android_bp =           "1.2.0"         // Updated:
    val threeten_bp =                   "1.4.0"         // Updated:
    val timber =                        "4.7.1"         // Updated:

    val android_annotation =            "1.1.0-rc01"    // Updated: May 7, 2019
    val android_constraint_layout =     "2.0.0-beta1"   // Updated: May 8, 2019
    val android_cue =                   "1.1"           // Updated: Jan 28, 2018
    val android_espresso =              "3.2.0"         // Updated: May 30, 2019
    val android_exo_player =            "2.10.1"        // Updated: May 20, 2019
    val android_fastlane_screengrab =   "1.2.0"         // Updated:
    val android_gradle_plugin =         "3.5.0-beta03"  // Updated: May 28, 2019
    val android_ktx =                   "1.1.0-beta01"  // Updated: May 8, 2019
    val android_lifecycle =             "2.1.0-beta01"  // Updated: May 8, 2019
    val android_material =              "1.1.0-alpha07" // Updated: May 31, 2019
    val android_material_bottom_bar =   "1.2-beta-6"    // Updated: Apr 23, 2019
    val android_navigation =            "2.1.0-alpha04" // Updated: May 18, 2019
    val android_paging =                "2.1.0"         // Updated: Jan 26, 2019
    val android_robolectric =           "4.3"           // Updated: May 30, 2019
    val android_room =                  "2.1.0-rc01"    // Updated: May 30, 2019
    val android_support =               "1.0.2"         // Updated: Nov 17, 2018
    val android_test_core =             "1.2.0"         // Updated: May 31, 2019
    val android_test_fragment =         "1.1.0-alpha09" // Updated: May 18, 2019
    val android_theia =                 "0.3-alpha-7"   // Updated: Apr 23, 2019
    val android_view_pager2 =           "1.0.0-alpha04" // Updated: May 8, 2019
    val android_view_state_store =      "1.3-beta-1"    // Updated: May 22, 2019
    val android_work =                  "2.1.0-alpha02" // Updated: May 18, 2019
    val androidx_core =                 "1.1.0-beta01"  // Updated: May 08, 2019
    val androidx_test =                 "1.2.0"         // Updated: May 31, 2019
}

@Suppress("unused")
object Libs {

    /* Kotlin */
    val kotlin =                                "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    val coroutines =                            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    val coroutines_android =                    "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
    val serialization =                         "org.jetbrains.kotlinx:kotlinx-serialization-runtime:${Versions.serialization}"
    val reflect =                               "org.jetbrains.kotlin:kotlin-reflect:${Versions.kotlin}"
    val test =                                  "org.jetbrains.kotlin:kotlin-test:${Versions.kotlin}"
    val test_junit =                            "org.jetbrains.kotlin:kotlin-test-junit:${Versions.kotlin}"

    val firebase_core_android =                 "com.google.firebase:firebase-core:${Versions.firebase_core_android}"
    val firebase_crashlytics_android =          "com.crashlytics.sdk.android:crashlytics:${Versions.firebase_crashlytics_android}"
    val koin =                                  "org.koin:koin-core:${Versions.koin}"
    val koin_android =                          "org.koin:koin-android:${Versions.koin}"
    val koin_android_viewmodel =                "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    val ktor =                                  "io.ktor:ktor-client-core:${Versions.ktor}"
    val ktor_android =                          "io.ktor:ktor-client-android:${Versions.ktor}"
    val mockk =                                 "io.mockk:mockk:${Versions.mockk}"
    val mockk_android =                         "io.mockk:mockk-android:${Versions.mockk}"
    val sqldelight =                            "com.squareup.sqldelight:runtime-jvm:${Versions.sqldelight}"
    val sqldelight_android_driver =             "com.squareup.sqldelight:android-driver:${Versions.sqldelight}"
    val sqldelight_android_paging =             "com.squareup.sqldelight:android-paging-extensions:${Versions.sqldelight}"
    val threeten_android_bp =                   "com.jakewharton.threetenabp:threetenabp:${Versions.threeten_android_bp}"
    val threeten_bp =                           "org.threeten:threetenbp:${Versions.threeten_bp}"
    val timber_android =                        "com.jakewharton.timber:timber:${Versions.timber}"

    /* Android */
    object Android {
        val androidx_core =                     "androidx.core:core:${Versions.androidx_core}"
        val annotation =                        "androidx.annotation:annotation:${Versions.android_annotation}"
        val appcompat =                         "androidx.appcompat:appcompat:${Versions.android_support}"
        val constraint_layout =                 "androidx.constraintlayout:constraintlayout:${Versions.android_constraint_layout}"
        val cue =                               "com.fxn769:cue:${Versions.android_cue}"
        val design =                            "com.android.support:design:${Versions.android_support}"
        val espresso =                          "androidx.test.espresso:espresso-core:${Versions.android_espresso}"
        val exo_player =                        "com.google.android.exoplayer:exoplayer:${Versions.android_exo_player}"
        val exo_player_rtmp =                   "com.google.android.exoplayer:extension-rtmp:${Versions.android_exo_player}"
        val fastlane_screengrab =               "tools.fastlane:screengrab:${Versions.android_fastlane_screengrab}"
        val ktx =                               "androidx.core:core-ktx:${Versions.android_ktx}"
        val lifecycle_compiler =                "androidx.lifecycle:lifecycle-compiler:${Versions.android_lifecycle}"
        val lifecycle_extensions =              "androidx.lifecycle:lifecycle-extensions:${Versions.android_lifecycle}"
        val lifecycle_jdk8 =                    "androidx.lifecycle:lifecycle-common-java8:${Versions.android_lifecycle}"
        val lifecycle_runtime =                 "androidx.lifecycle:lifecycle-runtime:${Versions.android_lifecycle}"
        val lifecycle_viewmodel =               "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.android_lifecycle}"
        val material =                          "com.google.android.material:material:${Versions.android_material}"
        val material_bottom_bar =               "studio.forface.materialbottombar:materialbottombar:${Versions.android_material_bottom_bar}"
        val material_bottom_bar_navigation =    "studio.forface.materialbottombar:materialbottombar-navigation:${Versions.android_material_bottom_bar}"
        val navigation_fragment =               "androidx.navigation:navigation-fragment-ktx:${Versions.android_navigation}"
        val navigation_ui =                     "androidx.navigation:navigation-ui-ktx:${Versions.android_navigation}"
        val paging =                            "androidx.paging:paging-runtime-ktx:${Versions.android_paging}"
        val robolectric =                       "org.robolectric:robolectric:${Versions.android_robolectric}"
        val room =                              "androidx.room:room-runtime:${Versions.android_room}"
        val room_compiler =                     "androidx.room:room-compiler:${Versions.android_room}"
        val room_coroutines =                   "androidx.room:room-coroutines:${Versions.android_room}"
        val room_testing =                      "androidx.room:room-testing:${Versions.android_room}"
        val support_annotations =               "com.android.support:support-annotations:28.0.0"
        val test_core =                         "androidx.test:core:${Versions.android_test_core}"
        val test_fragment =                     "androidx.fragment:fragment-testing:${Versions.android_test_fragment}"
        val test_rules =                        "androidx.test:rules:${Versions.androidx_test}"
        val test_runner =                       "androidx.test:runner:${Versions.androidx_test}"
        val theia =                             "studio.forface.theia:theia:${Versions.android_theia}"
        val view_pager2 =                       "androidx.viewpager2:viewpager2:${Versions.android_view_pager2}"
        val view_state_store =                  "studio.forface.viewstatestore:viewstatestore:${Versions.android_view_state_store}"
        val view_state_store_paging =           "studio.forface.viewstatestore:viewstatestore-paging:${Versions.android_view_state_store}"
        val work =                              "androidx.work:work-runtime-ktx:${Versions.android_work}"
    }
}