package com.example.kmpdemo.shared.ble

import android.content.Context

fun createAndroidBleController(context: Context): BleController {
    return AndroidBleController(context)
}
