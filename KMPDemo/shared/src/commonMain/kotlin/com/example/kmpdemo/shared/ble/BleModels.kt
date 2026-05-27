package com.example.kmpdemo.shared.ble

data class BleDevice(
    val address: String,
    val name: String?,
    val rssi: Int,
)

data class BleCharacteristicProperties(
    val isReadable: Boolean,
    val isWritable: Boolean,
    val isNotifiable: Boolean,
)

data class BleCharacteristicInfo(
    val uuid: String,
    val properties: BleCharacteristicProperties,
    val lastValueHex: String? = null,
)

data class BleServiceInfo(
    val uuid: String,
    val characteristics: List<BleCharacteristicInfo>,
)

enum class BleConnectionStatus {
    DISCONNECTED,
    CONNECTING,
    DISCOVERING_SERVICES,
    CONNECTED,
    FAILED,
}

data class BleConnectionState(
    val status: BleConnectionStatus = BleConnectionStatus.DISCONNECTED,
    val connectedDeviceAddress: String? = null,
    val connectedDeviceName: String? = null,
    val services: List<BleServiceInfo> = emptyList(),
    val errorMessage: String? = null,
)

data class BleRequirements(
    val hasBluetoothPermissions: Boolean = false,
    val isBluetoothEnabled: Boolean = false,
)

data class BleExplorerUiState(
    val requirements: BleRequirements = BleRequirements(),
    val isScanning: Boolean = false,
    val devices: List<BleDevice> = emptyList(),
    val connectionState: BleConnectionState = BleConnectionState(),
    val bannerMessage: String? = null,
) {
    val canStartScan: Boolean
        get() = requirements.hasBluetoothPermissions && requirements.isBluetoothEnabled && !isScanning

    val canConnect: Boolean
        get() = requirements.hasBluetoothPermissions && requirements.isBluetoothEnabled
}
