import Foundation
import SharedBle

@MainActor
final class IOSBleExplorerViewModel: ObservableObject {
    @Published private(set) var uiState: IOSBleExplorerViewState

    private let host: IosBleExplorerHost
    private var stopObservingUiState: (() -> Void)?

    init(host: IosBleExplorerHost = IosBleExplorerHost()) {
        self.host = host
        self.uiState = IOSBleExplorerViewState(shared: host.getCurrentUiState())
        observeState()
        refreshBluetoothState()
    }

    deinit {
        stopObservingUiState?()
        host.dispose()
    }

    func refreshBluetoothState() {
        host.refreshSystemState()
    }

    func toggleScan() {
        if uiState.isScanning {
            host.stopScan()
            return
        }

        host.requestStartScan()
    }

    func connect(to identifier: String) {
        host.requestConnect(deviceAddress: identifier)
    }

    func disconnect() {
        host.requestDisconnect()
    }

    func readCharacteristic(serviceUUID: String, characteristicUUID: String) {
        host.requestReadCharacteristic(
            serviceUuid: serviceUUID,
            characteristicUuid: characteristicUUID
        )
    }

    func clearMessage() {
        host.clearMessage()
    }

    private func observeState() {
        stopObservingUiState = host.startObservingUiState { [weak self] state in
            guard let self else { return }
            self.uiState = IOSBleExplorerViewState(shared: state)
        }
    }
}

struct IOSBleExplorerViewState {
    let hasBluetoothPermissions: Bool
    let isBluetoothEnabled: Bool
    let isScanning: Bool
    let devices: [IOSBleDevice]
    let connection: IOSBleConnectionState
    let bannerMessage: String?

    init(
        hasBluetoothPermissions: Bool = false,
        isBluetoothEnabled: Bool = false,
        isScanning: Bool = false,
        devices: [IOSBleDevice] = [],
        connection: IOSBleConnectionState = IOSBleConnectionState(),
        bannerMessage: String? = nil
    ) {
        self.hasBluetoothPermissions = hasBluetoothPermissions
        self.isBluetoothEnabled = isBluetoothEnabled
        self.isScanning = isScanning
        self.devices = devices
        self.connection = connection
        self.bannerMessage = bannerMessage
    }

    init(shared: BleExplorerUiState) {
        hasBluetoothPermissions = shared.requirements.hasBluetoothPermissions
        isBluetoothEnabled = shared.requirements.isBluetoothEnabled
        isScanning = shared.isScanning
        devices = shared.devices.compactMap { $0 as? BleDevice }.map(IOSBleDevice.init(shared:))
        connection = IOSBleConnectionState(shared: shared.connectionState)
        bannerMessage = shared.bannerMessage
    }

    var canStartScan: Bool {
        hasBluetoothPermissions && isBluetoothEnabled && !isScanning
    }

    var canConnect: Bool {
        hasBluetoothPermissions && isBluetoothEnabled
    }
}

struct IOSBleDevice: Identifiable {
    let id: String
    let name: String
    let rssi: Int
    let signalLabel: String

    init(shared: BleDevice) {
        id = shared.address
        name = shared.name ?? "Unnamed device"
        rssi = Int(shared.rssi)
        signalLabel = IOSBleDevice.describeSignalStrength(rssi)
    }

    private static func describeSignalStrength(_ value: Int) -> String {
        if value >= -55 {
            return "Strong"
        }
        if value >= -70 {
            return "Medium"
        }
        return "Weak"
    }
}

struct IOSBleConnectionState {
    let status: String
    let connectedDeviceIdentifier: String?
    let connectedDeviceName: String?
    let services: [IOSBleService]

    init(
        status: String = "Disconnected",
        connectedDeviceIdentifier: String? = nil,
        connectedDeviceName: String? = nil,
        services: [IOSBleService] = []
    ) {
        self.status = status
        self.connectedDeviceIdentifier = connectedDeviceIdentifier
        self.connectedDeviceName = connectedDeviceName
        self.services = services
    }

    init(shared: BleConnectionState) {
        status = shared.status.name
            .lowercased()
            .replacingOccurrences(of: "_", with: " ")
            .capitalized
        connectedDeviceIdentifier = shared.connectedDeviceAddress
        connectedDeviceName = shared.connectedDeviceName
        services = shared.services.compactMap { $0 as? BleServiceInfo }.map(IOSBleService.init(shared:))
    }
}

struct IOSBleService: Identifiable {
    let id: String
    let characteristics: [IOSBleCharacteristic]

    init(shared: BleServiceInfo) {
        id = shared.uuid
        characteristics = shared.characteristics
            .compactMap { $0 as? BleCharacteristicInfo }
            .map(IOSBleCharacteristic.init(shared:))
    }
}

struct IOSBleCharacteristic: Identifiable {
    let id: String
    let isReadable: Bool
    let isWritable: Bool
    let isNotifiable: Bool
    let lastValueHex: String?

    init(shared: BleCharacteristicInfo) {
        id = shared.uuid
        isReadable = shared.properties.isReadable
        isWritable = shared.properties.isWritable
        isNotifiable = shared.properties.isNotifiable
        lastValueHex = shared.lastValueHex
    }
}
