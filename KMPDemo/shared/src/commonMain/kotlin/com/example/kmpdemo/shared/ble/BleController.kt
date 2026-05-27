package com.example.kmpdemo.shared.ble

import kotlinx.coroutines.flow.StateFlow

data class BleActionResult(
    val isSuccessful: Boolean,
    val message: String? = null,
) {
    companion object {
        fun success(message: String? = null): BleActionResult = BleActionResult(
            isSuccessful = true,
            message = message,
        )

        fun failure(message: String): BleActionResult = BleActionResult(
            isSuccessful = false,
            message = message,
        )
    }
}

interface BleController {
    val scannedDevices: StateFlow<List<BleDevice>>
    val connectionState: StateFlow<BleConnectionState>

    suspend fun startScan(): BleActionResult
    fun stopScan()
    suspend fun connect(deviceAddress: String): BleActionResult
    suspend fun disconnect(): BleActionResult
    suspend fun readCharacteristic(
        serviceUuid: String,
        characteristicUuid: String,
    ): BleActionResult
}
