package com.dev.anasmohammed.corex.permission.core.enums

import android.os.Build

/**
 * Sealed class representing various Android versions.
 * Each Android version is associated with its SDK version and name.
 *
 * This class is used to manage and reference specific Android versions in a type-safe manner.
 * By using a sealed class, all possible Android versions are explicitly defined, ensuring easy
 * extensibility for future Android versions while maintaining compile-time safety.
 *
 * This sealed class can be used for handling version-specific logic, making it easier to implement
 * version checks or handle version-dependent features in an Android app.
 */
sealed class AndroidVersions(val sdk: Int, val name: String) {
    data object KitKatWatch4 : AndroidVersions(Build.VERSION_CODES.KITKAT_WATCH, "KitKatWatch4")
    data object Marshmallow6 : AndroidVersions(Build.VERSION_CODES.M, "KitKatWatch4")
    data object Oreo8 : AndroidVersions(Build.VERSION_CODES.O, "Oreo8")
    data object Pie9 : AndroidVersions(Build.VERSION_CODES.P, "Pie9")
    data object QuinceTart10 : AndroidVersions(Build.VERSION_CODES.Q, "QuinceTart10")
    data object RedVelvetCake11 : AndroidVersions(Build.VERSION_CODES.R, "RedVelvetCake11")
    data object SnowCone12 : AndroidVersions(Build.VERSION_CODES.S , "SnowCone12")
    data object SnowCone12v2 : AndroidVersions(Build.VERSION_CODES.S_V2 , "SnowCone12v2")
    data object Tiramisu13 : AndroidVersions(Build.VERSION_CODES.TIRAMISU , "Tiramisu13")
    data object UpsideDownCake14 : AndroidVersions(Build.VERSION_CODES.UPSIDE_DOWN_CAKE , "UpsideDownCake14")
    data object VanillaIceCream15 : AndroidVersions(Build.VERSION_CODES.VANILLA_ICE_CREAM , "VanillaIceCream15")
    data object Baklava16 : AndroidVersions(36 , "Baklava16")
}