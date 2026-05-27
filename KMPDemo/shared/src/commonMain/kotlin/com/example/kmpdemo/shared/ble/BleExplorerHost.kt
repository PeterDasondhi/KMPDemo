package com.example.kmpdemo.shared.ble

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

class BleExplorerHost(
    bleController: BleController,
    scope: CoroutineScope,
) {
    private val presenter = BleExplorerPresenter(
        bleController = bleController,
        scope = scope,
    )

    @NativeCoroutinesState
    val uiState: StateFlow<BleExplorerUiState> = presenter.uiState

    fun updateSystemState(
        hasBluetoothPermissions: Boolean,
        isBluetoothEnabled: Boolean,
    ) {
        presenter.updateSystemState(
            hasBluetoothPermissions = hasBluetoothPermissions,
            isBluetoothEnabled = isBluetoothEnabled,
        )
    }

    @NativeCoroutines
    suspend fun startScan() {
        presenter.startScan()
    }

    fun stopScan() {
        presenter.stopScan()
    }

    @NativeCoroutines
    suspend fun connect(deviceAddress: String) {
        presenter.connect(deviceAddress)
    }

    @NativeCoroutines
    suspend fun disconnect() {
        presenter.disconnect()
    }

    @NativeCoroutines
    suspend fun readCharacteristic(
        serviceUuid: String,
        characteristicUuid: String,
    ) {
        presenter.readCharacteristic(
            serviceUuid = serviceUuid,
            characteristicUuid = characteristicUuid,
        )
    }

    fun clearMessage() {
        presenter.clearMessage()
    }
}
