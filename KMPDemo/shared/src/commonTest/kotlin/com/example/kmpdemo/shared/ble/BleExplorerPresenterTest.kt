package com.example.kmpdemo.shared.ble

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BleExplorerPresenterTest {
    @Test
    fun startScan_requiresPermissions() = runTest {
        val controller = FakeBleController()
        val presenter = BleExplorerPresenter(
            bleController = controller,
            scope = backgroundScope,
        )

        presenter.updateSystemState(
            hasBluetoothPermissions = false,
            isBluetoothEnabled = true,
        )
        presenter.startScan()
        advanceUntilIdle()

        assertFalse(presenter.uiState.value.isScanning)
        assertEquals("Bluetooth permissions are required.", presenter.uiState.value.bannerMessage)
    }

    @Test
    fun startScan_requiresBluetoothToBeEnabled() = runTest {
        val controller = FakeBleController()
        val presenter = BleExplorerPresenter(
            bleController = controller,
            scope = backgroundScope,
        )

        presenter.updateSystemState(
            hasBluetoothPermissions = true,
            isBluetoothEnabled = false,
        )
        presenter.startScan()
        advanceUntilIdle()

        assertFalse(presenter.uiState.value.isScanning)
        assertEquals("Bluetooth must be enabled.", presenter.uiState.value.bannerMessage)
    }

    @Test
    fun successfulScan_setsScanningAndSortsDevicesBySignalStrength() = runTest {
        val controller = FakeBleController()
        val presenter = BleExplorerPresenter(
            bleController = controller,
            scope = backgroundScope,
        )

        presenter.updateSystemState(
            hasBluetoothPermissions = true,
            isBluetoothEnabled = true,
        )
        presenter.startScan()
        controller.scannedDevicesFlow.value = listOf(
            BleDevice(address = "AA:BB", name = "Weaker", rssi = -80),
            BleDevice(address = "CC:DD", name = "Stronger", rssi = -45),
        )
        advanceUntilIdle()

        assertTrue(presenter.uiState.value.isScanning)
        assertEquals(
            listOf("CC:DD", "AA:BB"),
            presenter.uiState.value.devices.map { it.address },
        )
    }

    @Test
    fun updateSystemState_stopsScanningWhenRequirementsChange() = runTest {
        val controller = FakeBleController()
        val presenter = BleExplorerPresenter(
            bleController = controller,
            scope = backgroundScope,
        )

        presenter.updateSystemState(
            hasBluetoothPermissions = true,
            isBluetoothEnabled = true,
        )
        presenter.startScan()
        presenter.updateSystemState(
            hasBluetoothPermissions = false,
            isBluetoothEnabled = true,
        )
        advanceUntilIdle()

        assertFalse(presenter.uiState.value.isScanning)
        assertEquals(1, controller.stopScanCalls)
    }

    @Test
    fun connect_propagatesConnectionStateFromController() = runTest {
        val controller = FakeBleController()
        val presenter = BleExplorerPresenter(
            bleController = controller,
            scope = backgroundScope,
        )

        presenter.updateSystemState(
            hasBluetoothPermissions = true,
            isBluetoothEnabled = true,
        )
        presenter.connect("AA:BB")
        controller.connectionStateFlow.value = BleConnectionState(
            status = BleConnectionStatus.CONNECTED,
            connectedDeviceAddress = "AA:BB",
            connectedDeviceName = "Heart Rate Sensor",
        )
        advanceUntilIdle()

        assertEquals("AA:BB", controller.lastConnectedAddress)
        assertEquals(BleConnectionStatus.CONNECTED, presenter.uiState.value.connectionState.status)
        assertEquals("Heart Rate Sensor", presenter.uiState.value.connectionState.connectedDeviceName)
    }
}

private class FakeBleController : BleController {
    val scannedDevicesFlow = MutableStateFlow<List<BleDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<BleDevice>> = scannedDevicesFlow

    val connectionStateFlow = MutableStateFlow(BleConnectionState())
    override val connectionState: StateFlow<BleConnectionState> = connectionStateFlow

    var startScanResult: BleActionResult = BleActionResult.success()
    var connectResult: BleActionResult = BleActionResult.success()
    var disconnectResult: BleActionResult = BleActionResult.success()
    var readResult: BleActionResult = BleActionResult.success()
    var lastConnectedAddress: String? = null
    var stopScanCalls: Int = 0

    override suspend fun startScan(): BleActionResult = startScanResult

    override fun stopScan() {
        stopScanCalls += 1
    }

    override suspend fun connect(deviceAddress: String): BleActionResult {
        lastConnectedAddress = deviceAddress
        return connectResult
    }

    override suspend fun disconnect(): BleActionResult = disconnectResult

    override suspend fun readCharacteristic(
        serviceUuid: String,
        characteristicUuid: String,
    ): BleActionResult = readResult
}
