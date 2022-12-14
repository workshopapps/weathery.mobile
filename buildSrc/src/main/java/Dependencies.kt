object Versions {
    const val compileSdkVersion = 33
    const val minSdkVersion = 21

    const val core_version = "1.7.0"
    const val appcompat_version = "1.5.1"
    const val material_comp_version = "1.7.0"
    const val unit_junit_test = "4.13.2"
    const val android_junit_test = "1.1.4"
    const val android_espresso_test = "3.5.0"
    const val nav_version = "2.5.1"
    const val dagger_hilt_version = "2.43.2"
    const val dagger_hiltcompiler_version = "1.0.0"
    const val constraints_version = "2.1.4"
    const val circle_image_version = "3.1.0"
    const val view_pager_version = "1.0.0"
    const val compose_version = "1.2.0"
    const val lifecycle_runtime_version = "2.5.1"
    const val activity_compose_version = "1.5.1"
    const val compose_foundation_version = "1.2.1"
    const val compose_VM_lifecycle_version = "2.6.0-alpha01"
    const val compose_navigation_version = "2.5.1"
    const val compose_constraint_layout_version = "1.0.1"
    const val compose_hilt_navigation_version = "1.0.0"
    const val dots_indicator_version = "4.3"
    const val dexter_version = "6.2.2"

    const val coroutine_core_version = "1.5.1"
    const val coroutine_android_version = "1.6.1"
    const val retrofit2_version ="2.9.0"
    const val retrofit2_converter_gson_version ="2.9.0"
    const val okhttp3_version ="5.0.0-alpha.3"
    const val okhttp3_logging_interceptor_version ="5.0.0-alpha.3"
    const val gson_version ="2.9.0"
    const val datastore_version ="1.0.0"

    const val room_version = "2.4.3"
}

object LibDependencies {

    //android
    val androidx_core = "androidx.core:core-ktx:${Versions.core_version}"
    val androidx_appcompat = "androidx.appcompat:appcompat:${Versions.appcompat_version}"
    val google_android_material =
        "com.google.android.material:material:${Versions.material_comp_version}"
    val constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraints_version}"

    //dagger hilt
    val dagger_hilt_android = "com.google.dagger:hilt-android:${Versions.dagger_hilt_version}"
    val dagger_hilt_android_compiler =
        "com.google.dagger:hilt-android-compiler:${Versions.dagger_hilt_version}"
    val hilt_hilt_compiler = "androidx.hilt:hilt-compiler:${Versions.dagger_hiltcompiler_version}"


    //navigation
    val navigation_fragment_ktx =
        "androidx.navigation:navigation-fragment-ktx:${Versions.nav_version}"
    val navigation_ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.nav_version}"
    val feature_module_support_navigation =
        "androidx.navigation:navigation-dynamic-features-fragment:${Versions.nav_version}"

    // datastore
    val datastore = "androidx.datastore:datastore-preferences:${Versions.datastore_version}"

    //unit test
    val junit = "junit:junit:${Versions.unit_junit_test}"

    // android test
    val test_ext_junit = "androidx.test.ext:junit:${Versions.android_junit_test}"
    val test_espresso_core =
        "androidx.test.espresso:espresso-core:${Versions.android_espresso_test}"

    //libs
    val circle_image_view = "de.hdodenhof:circleimageview:${Versions.circle_image_version}"
    val view_pager2 = "androidx.viewpager2:viewpager2:${Versions.view_pager_version}"
    val dots_indicator = "com.tbuonomo:dotsindicator:${Versions.dots_indicator_version}"
    val dexter = "com.karumi:dexter:${Versions.dexter_version}"

    // Coroutines
    val coroutine_core =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine_core_version}"
    val coroutine_android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine_android_version}"

// Retrofit
    val retrofit2 = "com.squareup.retrofit2:retrofit:${Versions.retrofit2_version}"
    val retrofit2_converter_gson ="com.squareup.retrofit2:converter-gson:${Versions.retrofit2_converter_gson_version}"
    val okhttp3 = "com.squareup.okhttp3:okhttp:${Versions.okhttp3_version}"
    val okhttp3_logging_interceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okhttp3_logging_interceptor_version}"
    val gson = "com.google.code.gson:gson:${Versions.gson_version}"


    //compose
    val compose_ui = "androidx.compose.ui:ui:${Versions.compose_version}"
    val compose_material = "androidx.compose.material:material:${Versions.compose_version}"
    val compose_ui_tooling_preview =
        "androidx.compose.ui:ui-tooling-preview:${Versions.compose_version}"
    val lifecycle_runtime_ktx =
        "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle_runtime_version}"
    val compose_activity = "androidx.activity:activity-compose:${Versions.activity_compose_version}"
    val compose_foundation =
        "androidx.compose.foundation:foundation:${Versions.compose_foundation_version}"

    //
    val compose_viewmodel_lifecycle =
        "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.compose_VM_lifecycle_version}"
    val compose_navigation =
        "androidx.navigation:navigation-compose:${Versions.compose_navigation_version}"
    val compose_constraint_layout =
        "androidx.constraintlayout:constraintlayout-compose:${Versions.compose_constraint_layout_version}"
    val compose_material_icons_extended =
        "androidx.compose.material:material-icons-extended:${Versions.compose_version}"
    val compose_hilt_navigation =
        "androidx.hilt:hilt-navigation-compose:${Versions.compose_hilt_navigation_version}"

    // compose test
    val compose_ui_test = "androidx.compose.ui:ui-test-junit4:${Versions.compose_version}"
    val debug_compose_ui_tooling = "androidx.compose.ui:ui-tooling:${Versions.compose_version}"

    //Room
    val room_runtime = "androidx.room:room-runtime:${Versions.room_version}"
    val room_compiler = "androidx.room:room-compiler:${Versions.room_version}"
    val room_ktx = "androidx.room:room-ktx:${Versions.room_version}"
}