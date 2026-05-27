package com.example.kmpdemo.shared.ble

import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class IosBleExplorerHost {
    private val controller = IosBleController()
    private val scope = MainScope()
    private val host = BleExplorerHost(
        bleController = controller,
        scope = scope,
    )

    init {
        refreshSystemState()
    }

    private val uiState: StateFlow<BleExplorerUiState> = host.uiState

    fun getCurrentUiState(): BleExplorerUiState = uiState.value

    fun startObservingUiState(onUpdate: (BleExplorerUiState) -> Unit): () -> Unit {
        val job: Job = scope.launch {
            uiState.collect { state ->
                onUpdate(state)
            }
        }
        return { job.cancel() }
    }

    fun refreshSystemState() {
        host.updateSystemState(
            hasBluetoothPermissions = controller.hasBluetoothPermission(),
            isBluetoothEnabled = controller.isBluetoothEnabled(),
        )
    }

    fun requestStartScan() {
        scope.launch {
            refreshSystemState()
            host.startScan()
        }
    }

    fun stopScan() {
        host.stopScan()
    }

    fun requestConnect(deviceAddress: String) {
        scope.launch {
            refreshSystemState()
            host.connect(deviceAddress)
        }
    }

    fun requestDisconnect() {
        scope.launch {
            host.disconnect()
        }
    }

    fun requestReadCharacteristic(
        serviceUuid: String,
        characteristicUuid: String,
    ) {
        scope.launch {
            refreshSystemState()
            host.readCharacteristic(
                serviceUuid = serviceUuid,
                characteristicUuid = characteristicUuid,
            )
        }
    }

    fun clearMessage() {
        host.clearMessage()
    }

    fun dispose() {
        scope.cancel()
    }
}
