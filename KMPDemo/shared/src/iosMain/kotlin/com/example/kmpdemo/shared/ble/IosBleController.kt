package com.example.kmpdemo.shared.ble

import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import platform.CoreBluetooth.CBAdvertisementDataLocalNameKey
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBCharacteristicProperties
import platform.CoreBluetooth.CBCharacteristicPropertyNotify
import platform.CoreBluetooth.CBCharacteristicPropertyRead
import platform.CoreBluetooth.CBCharacteristicPropertyWrite
import platform.CoreBluetooth.CBCharacteristicPropertyWriteWithoutResponse
import platform.CoreBluetooth.CBCentralManager
import platform.CoreBluetooth.CBCentralManagerDelegateProtocol
import platform.CoreBluetooth.CBManagerAuthorization
import platform.CoreBluetooth.CBManagerAuthorizationAllowedAlways
import platform.CoreBluetooth.CBManagerStatePoweredOn
import platform.CoreBluetooth.CBPeripheral
import platform.CoreBluetooth.CBPeripheralDelegateProtocol
import platform.CoreBluetooth.CBService
import platform.Foundation.NSData
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSObject
import platform.posix.memcpy

class IosBleController : BleController {
    private val discoveredPeripherals = linkedMapOf<String, CBPeripheral>()
    private val characteristicLookup = linkedMapOf<String, CBCharacteristic>()

    private val _scannedDevices = MutableStateFlow<List<BleDevice>>(emptyList())
    override val scannedDevices: StateFlow<List<BleDevice>> = _scannedDevices.asStateFlow()

    private val _connectionState = MutableStateFlow(BleConnectionState())
    override val connectionState: StateFlow<BleConnectionState> = _connectionState.asStateFlow()

    private val delegate = AppleBleDelegate(owner = this)
    private val centralManager = CBCentralManager(
        delegate = delegate,
        queue = null,
    )

    private var activePeripheral: CBPeripheral? = null
    private var activeDeviceIdentifier: String? = null
    private var activeDeviceName: String? = null
    private var pendingCharacteristicDiscoveryCount: Int = 0

    private class AppleBleDelegate(
        private val owner: IosBleController,
    ) : NSObject(), CBCentralManagerDelegateProtocol, CBPeripheralDelegateProtocol {
        override fun centralManagerDidUpdateState(central: CBCentralManager) {
            owner.handleCentralManagerDidUpdateState(central)
        }

        override fun centralManager(
            central: CBCentralManager,
            didDiscoverPeripheral: CBPeripheral,
            advertisementData: Map<Any?, *>,
            RSSI: NSNumber,
        ) {
            owner.handleDidDiscoverPeripheral(
                didDiscoverPeripheral = didDiscoverPeripheral,
                advertisementData = advertisementData,
                RSSI = RSSI,
            )
        }

        override fun centralManager(
            central: CBCentralManager,
            didConnectPeripheral: CBPeripheral,
        ) {
            owner.handleDidConnectPeripheral(didConnectPeripheral)
        }

        @ObjCSignatureOverride
        override fun centralManager(
            central: CBCentralManager,
            didFailToConnectPeripheral: CBPeripheral,
            error: NSError?,
        ) {
            owner.handleDidFailToConnectPeripheral(
                didFailToConnectPeripheral = didFailToConnectPeripheral,
                error = error,
            )
        }

        @ObjCSignatureOverride
        override fun centralManager(
            central: CBCentralManager,
            didDisconnectPeripheral: CBPeripheral,
            error: NSError?,
        ) {
            owner.handleDidDisconnectPeripheral(
                didDisconnectPeripheral = didDisconnectPeripheral,
                error = error,
            )
        }

        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverServices: NSError?,
        ) {
            owner.handleDidDiscoverServices(
                peripheral = peripheral,
                error = didDiscoverServices,
            )
        }

        override fun peripheral(
            peripheral: CBPeripheral,
            didDiscoverCharacteristicsForService: CBService,
            error: NSError?,
        ) {
            owner.handleDidDiscoverCharacteristicsForService(
                peripheral = peripheral,
                service = didDiscoverCharacteristicsForService,
                error = error,
            )
        }

        override fun peripheral(
            peripheral: CBPeripheral,
            didUpdateValueForCharacteristic: CBCharacteristic,
            error: NSError?,
        ) {
            owner.handleDidUpdateValueForCharacteristic(
                peripheral = peripheral,
                characteristic = didUpdateValueForCharacteristic,
                error = error,
            )
        }
    }

    fun hasBluetoothPermission(): Boolean {
        return CBCentralManager.authorization() == CBManagerAuthorizationAllowedAlways
    }

    fun isBluetoothEnabled(): Boolean {
        return centralManager.state == CBManagerStatePoweredOn
    }

    override suspend fun startScan(): BleActionResult {
        if (!hasBluetoothPermission()) {
            return BleActionResult.failure("Bluetooth permissions are missing.")
        }

        if (!isBluetoothEnabled()) {
            return BleActionResult.failure("Bluetooth is not ready for scanning.")
        }

        if (centralManager.isScanning()) return BleActionResult.success()

        discoveredPeripherals.clear()
        _scannedDevices.value = emptyList()
        _connectionState.update { it.copy(errorMessage = null) }

        centralManager.scanForPeripheralsWithServices(
            serviceUUIDs = null,
            options = null,
        )

        return BleActionResult.success()
    }

    override fun stopScan() {
        if (centralManager.isScanning()) {
            centralManager.stopScan()
        }
    }

    override suspend fun connect(deviceAddress: String): BleActionResult {
        stopScan()

        if (!isBluetoothEnabled()) {
            return BleActionResult.failure("Bluetooth is unavailable on this device.")
        }

        val peripheral = discoveredPeripherals[deviceAddress]
            ?: return BleActionResult.failure("BLE device was not found.")

        activePeripheral?.let { existingPeripheral ->
            if (existingPeripheral != peripheral) {
                centralManager.cancelPeripheralConnection(existingPeripheral)
                closeActivePeripheral()
            }
        }

        activePeripheral = peripheral
        activeDeviceIdentifier = peripheral.identifier.UUIDString
        activeDeviceName = peripheral.name
        pendingCharacteristicDiscoveryCount = 0

        _connectionState.value = BleConnectionState(
            status = BleConnectionStatus.CONNECTING,
            connectedDeviceAddress = activeDeviceIdentifier,
            connectedDeviceName = activeDeviceName,
        )

        peripheral.delegate = delegate
        centralManager.connectPeripheral(
            peripheral = peripheral,
            options = null,
        )

        return BleActionResult.success()
    }

    override suspend fun disconnect(): BleActionResult {
        val peripheral = activePeripheral ?: return BleActionResult.success()
        centralManager.cancelPeripheralConnection(peripheral)
        return BleActionResult.success()
    }

    override suspend fun readCharacteristic(
        serviceUuid: String,
        characteristicUuid: String,
    ): BleActionResult {
        val key = characteristicKey(
            serviceUuid = serviceUuid,
            characteristicUuid = characteristicUuid,
        )
        val characteristic = characteristicLookup[key]
            ?: return BleActionResult.failure("Characteristic $characteristicUuid was not found.")

        if (!characteristic.hasProperty(CBCharacteristicPropertyRead)) {
            return BleActionResult.failure("Characteristic $characteristicUuid is not readable.")
        }

        activePeripheral?.readValueForCharacteristic(characteristic)
            ?: return BleActionResult.failure("Connect to a device first.")

        return BleActionResult.success("Reading characteristic value.")
    }

    private fun handleCentralManagerDidUpdateState(central: CBCentralManager) {
        if (central.state == CBManagerStatePoweredOn) return

        stopScan()
        _connectionState.value = BleConnectionState(
            status = BleConnectionStatus.DISCONNECTED,
            errorMessage = bluetoothStateMessage(central.state, CBCentralManager.authorization()),
        )
    }

    private fun handleDidDiscoverPeripheral(
        didDiscoverPeripheral: CBPeripheral,
        advertisementData: Map<Any?, *>,
        RSSI: NSNumber,
    ) {
        val identifier = didDiscoverPeripheral.identifier.UUIDString
        discoveredPeripherals[identifier] = didDiscoverPeripheral

        val existingName = _scannedDevices.value.firstOrNull { it.address == identifier }?.name
        val deviceName = advertisementData[CBAdvertisementDataLocalNameKey] as? String
            ?: didDiscoverPeripheral.name
            ?: existingName

        _scannedDevices.value = discoveredPeripherals.values.map { peripheral ->
            val peripheralIdentifier = peripheral.identifier.UUIDString
            val existingDevice = _scannedDevices.value.firstOrNull { it.address == peripheralIdentifier }
            BleDevice(
                address = peripheralIdentifier,
                name = if (peripheralIdentifier == identifier) deviceName else peripheral.name ?: existingDevice?.name,
                rssi = if (peripheralIdentifier == identifier) RSSI.intValue else existingDevice?.rssi ?: 0,
            )
        }
    }

    private fun handleDidConnectPeripheral(
        didConnectPeripheral: CBPeripheral,
    ) {
        if (didConnectPeripheral != activePeripheral) return

        pendingCharacteristicDiscoveryCount = 0
        _connectionState.value = BleConnectionState(
            status = BleConnectionStatus.DISCOVERING_SERVICES,
            connectedDeviceAddress = activeDeviceIdentifier,
            connectedDeviceName = activeDeviceName,
        )
        didConnectPeripheral.delegate = delegate
        didConnectPeripheral.discoverServices(null)
    }

    private fun handleDidFailToConnectPeripheral(
        didFailToConnectPeripheral: CBPeripheral,
        error: NSError?,
    ) {
        if (didFailToConnectPeripheral != activePeripheral) return

        _connectionState.value = BleConnectionState(
            status = BleConnectionStatus.FAILED,
            connectedDeviceAddress = activeDeviceIdentifier,
            connectedDeviceName = activeDeviceName,
            errorMessage = error?.localizedDescription ?: "Unable to open a BLE connection.",
        )
    }

    private fun handleDidDisconnectPeripheral(
        didDisconnectPeripheral: CBPeripheral,
        error: NSError?,
    ) {
        if (didDisconnectPeripheral != activePeripheral) return

        val identifier = activeDeviceIdentifier
        val name = activeDeviceName
        closeActivePeripheral()

        _connectionState.value = if (error == null) {
            BleConnectionState()
        } else {
            BleConnectionState(
                status = BleConnectionStatus.FAILED,
                connectedDeviceAddress = identifier,
                connectedDeviceName = name,
                errorMessage = error.localizedDescription ?: "Bluetooth disconnected unexpectedly.",
            )
        }
    }

    private fun handleDidDiscoverServices(
        peripheral: CBPeripheral,
        error: NSError?,
    ) {
        if (peripheral != activePeripheral) return

        if (error != null) {
            _connectionState.update {
                it.copy(
                    status = BleConnectionStatus.FAILED,
                    errorMessage = error.localizedDescription ?: "Service discovery failed.",
                )
            }
            return
        }

        val services = peripheral.services.orEmptyServices()
        if (services.isEmpty()) {
            _connectionState.update {
                it.copy(
                    status = BleConnectionStatus.CONNECTED,
                    services = emptyList(),
                    errorMessage = null,
                )
            }
            return
        }

        pendingCharacteristicDiscoveryCount = services.size
        characteristicLookup.clear()
        services.forEach { service ->
            peripheral.discoverCharacteristics(
                characteristicUUIDs = null,
                forService = service,
            )
        }
    }

    private fun handleDidDiscoverCharacteristicsForService(
        peripheral: CBPeripheral,
        service: CBService,
        error: NSError?,
    ) {
        if (peripheral != activePeripheral) return

        if (error != null) {
            _connectionState.update {
                it.copy(
                    status = BleConnectionStatus.FAILED,
                    errorMessage = error.localizedDescription ?: "Characteristic discovery failed.",
                )
            }
            return
        }

        service.characteristics
            .orEmptyCharacteristics()
            .forEach { characteristic ->
                val lookupKey = characteristicKey(
                    serviceUuid = service.UUID.UUIDString,
                    characteristicUuid = characteristic.UUID.UUIDString,
                )
                characteristicLookup[lookupKey] = characteristic
            }

        pendingCharacteristicDiscoveryCount -= 1
        if (pendingCharacteristicDiscoveryCount > 0) return

        _connectionState.update {
            it.copy(
                status = BleConnectionStatus.CONNECTED,
                services = buildServiceSnapshot(peripheral),
                errorMessage = null,
            )
        }
    }

    private fun handleDidUpdateValueForCharacteristic(
        peripheral: CBPeripheral,
        characteristic: CBCharacteristic,
        error: NSError?,
    ) {
        if (peripheral != activePeripheral) return

        if (error != null) {
            _connectionState.update {
                it.copy(errorMessage = error.localizedDescription ?: "Characteristic read failed.")
            }
            return
        }

        _connectionState.update {
            it.copy(
                services = buildServiceSnapshot(peripheral),
                errorMessage = null,
            )
        }
    }

    private fun buildServiceSnapshot(peripheral: CBPeripheral): List<BleServiceInfo> {
        return peripheral.services.orEmptyServices().map { service ->
            BleServiceInfo(
                uuid = service.UUID.UUIDString,
                characteristics = service.characteristics.orEmptyCharacteristics().map { characteristic ->
                    BleCharacteristicInfo(
                        uuid = characteristic.UUID.UUIDString,
                        properties = BleCharacteristicProperties(
                            isReadable = characteristic.hasProperty(CBCharacteristicPropertyRead),
                            isWritable = characteristic.hasProperty(CBCharacteristicPropertyWrite) ||
                                characteristic.hasProperty(CBCharacteristicPropertyWriteWithoutResponse),
                            isNotifiable = characteristic.hasProperty(CBCharacteristicPropertyNotify),
                        ),
                        lastValueHex = characteristic.value?.toHexString(),
                    )
                },
            )
        }
    }

    private fun closeActivePeripheral() {
        activePeripheral?.delegate = null
        activePeripheral = null
        activeDeviceIdentifier = null
        activeDeviceName = null
        pendingCharacteristicDiscoveryCount = 0
        characteristicLookup.clear()
    }
}

private fun bluetoothStateMessage(
    state: platform.CoreBluetooth.CBManagerState,
    authorization: CBManagerAuthorization,
): String {
    if (authorization != CBManagerAuthorizationAllowedAlways) {
        return "Bluetooth access is unavailable. Please allow Bluetooth access in Settings."
    }

    return if (state == CBManagerStatePoweredOn) {
        ""
    } else {
        "Bluetooth must be enabled on the device."
    }
}

private fun CBCharacteristic.hasProperty(property: CBCharacteristicProperties): Boolean {
    return (properties and property.toULong()) != 0uL
}

private fun characteristicKey(
    serviceUuid: String,
    characteristicUuid: String,
): String {
    return "$serviceUuid|$characteristicUuid"
}

private fun List<*>?.orEmptyServices(): List<CBService> {
    return this?.mapNotNull { it as? CBService }.orEmpty()
}

private fun List<*>?.orEmptyCharacteristics(): List<CBCharacteristic> {
    return this?.mapNotNull { it as? CBCharacteristic }.orEmpty()
}

private fun NSData.toHexString(): String {
    val bytes = ByteArray(length.toInt())
    bytes.usePinned { pinned ->
        memcpy(pinned.addressOf(0), this.bytes, length)
    }
    return bytes.joinToString(separator = " ") { byte ->
        byte.toUByte().toString(16).padStart(2, '0').uppercase()
    }
}
