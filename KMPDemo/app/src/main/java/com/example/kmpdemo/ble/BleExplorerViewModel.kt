package com.example.kmpdemo.ble

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kmpdemo.shared.ble.BleExplorerHost
import com.example.kmpdemo.shared.ble.BleExplorerUiState
import com.example.kmpdemo.shared.ble.createAndroidBleController
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BleExplorerViewModel(application: Application) : AndroidViewModel(application) {
    private val host = BleExplorerHost(
        bleController = createAndroidBleController(application),
        scope = viewModelScope,
    )

    val uiState: StateFlow<BleExplorerUiState> = host.uiState

    fun updateSystemState(
        hasBluetoothPermissions: Boolean,
        isBluetoothEnabled: Boolean,
    ) {
        host.updateSystemState(
            hasBluetoothPermissions = hasBluetoothPermissions,
            isBluetoothEnabled = isBluetoothEnabled,
        )
    }

    fun toggleScan() {
        viewModelScope.launch {
            if (uiState.value.isScanning) {
                host.stopScan()
            } else {
                host.startScan()
            }
        }
    }

    fun connect(deviceAddress: String) {
        viewModelScope.launch {
            host.connect(deviceAddress)
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            host.disconnect()
        }
    }

    fun readCharacteristic(
        serviceUuid: String,
        characteristicUuid: String,
    ) {
        viewModelScope.launch {
            host.readCharacteristic(
                serviceUuid = serviceUuid,
                characteristicUuid = characteristicUuid,
            )
        }
    }

    fun clearMessage() {
        host.clearMessage()
    }
}
