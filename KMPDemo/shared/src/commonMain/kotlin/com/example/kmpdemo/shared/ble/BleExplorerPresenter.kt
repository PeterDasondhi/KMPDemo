package com.example.kmpdemo.shared.ble

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class BleExplorerPresenter(
    private val bleController: BleController,
    scope: CoroutineScope,
) {
    private val requirements = MutableStateFlow(BleRequirements())
    private val isScanning = MutableStateFlow(false)
    private val transientMessage = MutableStateFlow<String?>(null)

    val uiState: StateFlow<BleExplorerUiState> = combine(
        bleController.scannedDevices,
        bleController.connectionState,
        requirements,
        isScanning,
        transientMessage,
    ) { scannedDevices, connectionState, currentRequirements, isCurrentlyScanning, message ->
        BleExplorerUiState(
            requirements = currentRequirements,
            isScanning = isCurrentlyScanning,
            devices = scannedDevices.sortedByDescending { it.rssi },
            connectionState = connectionState,
            bannerMessage = message ?: connectionState.errorMessage,
        )
    }.stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = BleExplorerUiState(),
    )

    fun updateSystemState(
        hasBluetoothPermissions: Boolean,
        isBluetoothEnabled: Boolean,
    ) {
        requirements.value = BleRequirements(
            hasBluetoothPermissions = hasBluetoothPermissions,
            isBluetoothEnabled = isBluetoothEnabled,
        )

        if ((!hasBluetoothPermissions || !isBluetoothEnabled) && isScanning.value) {
            bleController.stopScan()
            isScanning.value = false
        }
    }

    suspend fun startScan() {
        transientMessage.value = null
        if (!ensureReadyForBluetoothAction()) return

        val result = bleController.startScan()
        if (!result.isSuccessful) {
            transientMessage.value = result.message ?: "Unable to start BLE scan."
            return
        }

        isScanning.value = true
    }

    fun stopScan() {
        bleController.stopScan()
        isScanning.value = false
    }

    suspend fun connect(deviceAddress: String) {
        transientMessage.value = null
        if (!ensureReadyForBluetoothAction()) return

        stopScan()
        val result = bleController.connect(deviceAddress)
        if (!result.isSuccessful) transientMessage.value = result.message ?: "Unable to connect to device."
    }

    suspend fun disconnect() {
        transientMessage.value = null
        val result = bleController.disconnect()
        if (!result.isSuccessful) transientMessage.value = result.message ?: "Unable to disconnect from device."
    }

    suspend fun readCharacteristic(
        serviceUuid: String,
        characteristicUuid: String,
    ) {
        transientMessage.value = null
        if (!ensureReadyForBluetoothAction()) return

        val result = bleController.readCharacteristic(
            serviceUuid = serviceUuid,
            characteristicUuid = characteristicUuid,
        )
        if (!result.isSuccessful) {
            transientMessage.value = result.message ?: "Unable to read characteristic."
        }
    }

    fun clearMessage() {
        transientMessage.value = null
    }

    private fun ensureReadyForBluetoothAction(): Boolean {
        if (!requirements.value.hasBluetoothPermissions) {
            transientMessage.value = "Bluetooth permissions are required."
            return false
        }

        if (!requirements.value.isBluetoothEnabled) {
            transientMessage.value = "Bluetooth must be enabled."
            return false
        }

        return true
    }
}
