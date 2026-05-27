package com.example.kmpdemo.shared.ble

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.bluetooth.le.BluetoothLeScanner
import android.content.Context
import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

class AndroidBleController(context: Context) : BleController {
    private val appContext = context.applicationContext
    private val bluetoothManager = appContext.getSystemService(BluetoothManager::class.java)
    private val bluetoothAdapter: BluetoothAdapter?
        get() = bluetoothManager?.adapter
    private val bluetoothLeScanner: BluetoothLeScanner?
        get() = bluetoothAdapter?.bluetoothLeScanner

    private val discoveredDevices = linkedMapOf<String, BleDevice>()

    private val _scannedDevices = MutableStateFlow<List<BleDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<BleDevice>> = _scannedDevices.asStateFlow()

    private val _connectionState = MutableStateFlow(BleConnectionState())
    override val connectionState: StateFlow<BleConnectionState> = _connectionState.asStateFlow()

    private var activeScanCallback: ScanCallback? = null
    private var activeGatt: BluetoothGatt? = null
    private var activeDeviceAddress: String? = null
    private var activeDeviceName: String? = null

    @SuppressLint("MissingPermission")
    override suspend fun startScan(): BleActionResult {
        if (activeScanCallback != null) return BleActionResult.success()

        val scanner = bluetoothLeScanner ?: return BleActionResult.failure(
            message = "Bluetooth LE scanning is unavailable on this device.",
        )

        discoveredDevices.clear()
        _scannedDevices.value = emptyList()
        _connectionState.update { it.copy(errorMessage = null) }

        val callback = object : ScanCallback() {
            override fun onScanResult(
                callbackType: Int,
                result: ScanResult,
            ) {
                upsertDevice(result)
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>) {
                results.forEach(::upsertDevice)
            }

            override fun onScanFailed(errorCode: Int) {
                _connectionState.update {
                    it.copy(errorMessage = "BLE scan failed with error code $errorCode.")
                }
                stopScan()
            }
        }

        activeScanCallback = callback

        return try {
            scanner.startScan(
                emptyList<ScanFilter>(),
                ScanSettings.Builder()
                    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .build(),
                callback,
            )
            BleActionResult.success()
        } catch (exception: SecurityException) {
            activeScanCallback = null
            BleActionResult.failure("Bluetooth permissions are missing.")
        } catch (exception: IllegalStateException) {
            activeScanCallback = null
            BleActionResult.failure("Bluetooth is not ready for scanning.")
        }
    }

    @SuppressLint("MissingPermission")
    override fun stopScan() {
        val callback = activeScanCallback ?: return
        runCatching {
            bluetoothLeScanner?.stopScan(callback)
        }
        activeScanCallback = null
    }

    @SuppressLint("MissingPermission")
    override suspend fun connect(deviceAddress: String): BleActionResult {
        stopScan()

        val adapter = bluetoothAdapter ?: return BleActionResult.failure(
            message = "Bluetooth is unavailable on this device.",
        )

        val device = runCatching {
            adapter.getRemoteDevice(deviceAddress)
        }.getOrNull() ?: return BleActionResult.failure("Invalid BLE device address.")

        closeActiveGatt()
        activeDeviceAddress = device.address
        activeDeviceName = discoveredDevices[device.address]?.name

        _connectionState.value = BleConnectionState(
            status = BleConnectionStatus.CONNECTING,
            connectedDeviceAddress = activeDeviceAddress,
            connectedDeviceName = activeDeviceName,
        )

        activeGatt = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                device.connectGatt(
                    appContext,
                    false,
                    gattCallback,
                    BluetoothDevice.TRANSPORT_LE,
                )
            } else {
                device.connectGatt(appContext, false, gattCallback)
            }
        } catch (exception: SecurityException) {
            null
        }

        if (activeGatt == null) {
            _connectionState.value = BleConnectionState(
                status = BleConnectionStatus.FAILED,
                connectedDeviceAddress = activeDeviceAddress,
                connectedDeviceName = activeDeviceName,
                errorMessage = "Unable to open a GATT connection.",
            )
            return BleActionResult.failure("Unable to open a GATT connection.")
        }

        return BleActionResult.success()
    }

    @SuppressLint("MissingPermission")
    override suspend fun disconnect(): BleActionResult {
        val gatt = activeGatt ?: return BleActionResult.success()
        return try {
            gatt.disconnect()
            BleActionResult.success()
        } catch (exception: SecurityException) {
            BleActionResult.failure("Bluetooth permissions are missing.")
        }
    }

    @SuppressLint("MissingPermission")
    override suspend fun readCharacteristic(
        serviceUuid: String,
        characteristicUuid: String,
    ): BleActionResult {
        val gatt = activeGatt ?: return BleActionResult.failure("Connect to a device first.")
        val service = runCatching {
            gatt.getService(UUID.fromString(serviceUuid))
        }.getOrNull() ?: return BleActionResult.failure("Service $serviceUuid was not found.")

        val characteristic = runCatching {
            service.getCharacteristic(UUID.fromString(characteristicUuid))
        }.getOrNull() ?: return BleActionResult.failure(
            "Characteristic $characteristicUuid was not found.",
        )

        if (!characteristic.hasProperty(BluetoothGattCharacteristic.PROPERTY_READ)) {
            return BleActionResult.failure("Characteristic $characteristicUuid is not readable.")
        }

        return try {
            if (gatt.readCharacteristic(characteristic)) {
                BleActionResult.success("Reading characteristic value.")
            } else {
                BleActionResult.failure("Characteristic read could not be started.")
            }
        } catch (exception: SecurityException) {
            BleActionResult.failure("Bluetooth permissions are missing.")
        }
    }

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(
            gatt: BluetoothGatt,
            status: Int,
            newState: Int,
        ) {
            if (gatt != activeGatt) return

            val deviceAddress = activeDeviceAddress
            val deviceName = activeDeviceName

            if (status != BluetoothGatt.GATT_SUCCESS) {
                closeActiveGatt()
                _connectionState.value = BleConnectionState(
                    status = BleConnectionStatus.FAILED,
                    connectedDeviceAddress = deviceAddress,
                    connectedDeviceName = deviceName,
                    errorMessage = "Connection failed with status $status.",
                )
                return
            }

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                _connectionState.value = BleConnectionState(
                    status = BleConnectionStatus.DISCOVERING_SERVICES,
                    connectedDeviceAddress = deviceAddress,
                    connectedDeviceName = deviceName,
                )

                if (!gatt.discoverServices()) {
                    _connectionState.value = BleConnectionState(
                        status = BleConnectionStatus.FAILED,
                        connectedDeviceAddress = deviceAddress,
                        connectedDeviceName = deviceName,
                        errorMessage = "Service discovery could not be started.",
                    )
                }
                return
            }

            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                closeActiveGatt()
                _connectionState.value = BleConnectionState()
            }
        }

        override fun onServicesDiscovered(
            gatt: BluetoothGatt,
            status: Int,
        ) {
            if (gatt != activeGatt) return

            if (status != BluetoothGatt.GATT_SUCCESS) {
                _connectionState.update {
                    it.copy(
                        status = BleConnectionStatus.FAILED,
                        errorMessage = "Service discovery failed with status $status.",
                    )
                }
                return
            }

            _connectionState.update {
                it.copy(
                    status = BleConnectionStatus.CONNECTED,
                    services = gatt.services.map(::toServiceInfo),
                    errorMessage = null,
                )
            }
        }

        @Deprecated("Deprecated in Android API 33")
        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            status: Int,
        ) {
            handleCharacteristicRead(
                characteristic = characteristic,
                value = characteristic.value,
                status = status,
            )
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int,
        ) {
            handleCharacteristicRead(
                characteristic = characteristic,
                value = value,
                status = status,
            )
        }
    }

    private fun upsertDevice(result: ScanResult) {
        val address = result.device.address ?: return
        val existingDevice = discoveredDevices[address]
        val device = BleDevice(
            address = address,
            name = result.scanRecord?.deviceName ?: existingDevice?.name ?: runCatching {
                result.device.name
            }.getOrNull(),
            rssi = result.rssi,
        )
        discoveredDevices[address] = device
        _scannedDevices.value = discoveredDevices.values.toList()
    }

    private fun handleCharacteristicRead(
        characteristic: BluetoothGattCharacteristic,
        value: ByteArray?,
        status: Int,
    ) {
        if (status != BluetoothGatt.GATT_SUCCESS) {
            _connectionState.update {
                it.copy(errorMessage = "Characteristic read failed with status $status.")
            }
            return
        }

        val serviceUuid = characteristic.service.uuid.toString()
        val characteristicUuid = characteristic.uuid.toString()
        val formattedValue = value?.toHexString()

        _connectionState.update { currentState ->
            currentState.copy(
                services = currentState.services.map { serviceInfo ->
                    if (serviceInfo.uuid != serviceUuid) return@map serviceInfo

                    serviceInfo.copy(
                        characteristics = serviceInfo.characteristics.map { characteristicInfo ->
                            if (characteristicInfo.uuid != characteristicUuid) return@map characteristicInfo

                            characteristicInfo.copy(lastValueHex = formattedValue)
                        },
                    )
                },
                errorMessage = null,
            )
        }
    }

    private fun toServiceInfo(service: android.bluetooth.BluetoothGattService): BleServiceInfo {
        return BleServiceInfo(
            uuid = service.uuid.toString(),
            characteristics = service.characteristics.map(::toCharacteristicInfo),
        )
    }

    private fun toCharacteristicInfo(
        characteristic: BluetoothGattCharacteristic,
    ): BleCharacteristicInfo {
        return BleCharacteristicInfo(
            uuid = characteristic.uuid.toString(),
            properties = BleCharacteristicProperties(
                isReadable = characteristic.hasProperty(BluetoothGattCharacteristic.PROPERTY_READ),
                isWritable = characteristic.hasProperty(
                    BluetoothGattCharacteristic.PROPERTY_WRITE,
                ) || characteristic.hasProperty(BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE),
                isNotifiable = characteristic.hasProperty(BluetoothGattCharacteristic.PROPERTY_NOTIFY),
            ),
        )
    }

    private fun closeActiveGatt() {
        activeGatt?.close()
        activeGatt = null
        activeDeviceAddress = null
        activeDeviceName = null
    }
}

private fun BluetoothGattCharacteristic.hasProperty(property: Int): Boolean {
    return properties and property != 0
}

private fun ByteArray.toHexString(): String {
    return joinToString(separator = " ") { byte ->
        "%02X".format(byte.toInt() and 0xFF)
    }
}
