plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.protobuf) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.android.junit5) apply false

    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}
