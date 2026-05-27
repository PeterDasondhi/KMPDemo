import SwiftUI
import UIKit

struct BleExplorerView: View {
    @ObservedObject var viewModel: IOSBleExplorerViewModel

    var body: some View {
        List {
            if !viewModel.uiState.hasBluetoothPermissions {
                Section {
                    requirementCard(
                        title: "Bluetooth access required",
                        description: "iOS requires Bluetooth access before scanning or connecting to nearby accessories.",
                        actionTitle: "Open Settings",
                        action: openSettings
                    )
                }
            }

            if !viewModel.uiState.isBluetoothEnabled {
                Section {
                    requirementCard(
                        title: "Bluetooth is turned off",
                        description: "Turn Bluetooth back on from Control Center or Settings to continue.",
                        actionTitle: "Refresh Status",
                        action: viewModel.refreshBluetoothState
                    )
                }
            }

            if let message = viewModel.uiState.bannerMessage {
                Section {
                    HStack(alignment: .top, spacing: 12) {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .foregroundStyle(.orange)
                            .accessibilityHidden(true)
                        VStack(alignment: .leading, spacing: 8) {
                            Text(message)
                                .font(.body)
                            Button("Dismiss") {
                                viewModel.clearMessage()
                            }
                        }
                    }
                    .padding(.vertical, 6)
                    .accessibilityElement(children: .combine)
                }
            }

            Section("Scan Controls") {
                Button(viewModel.uiState.isScanning ? "Stop Scan" : "Start Scan") {
                    viewModel.toggleScan()
                }
                .buttonStyle(.borderedProminent)
                .disabled(!viewModel.uiState.isScanning && !viewModel.uiState.canStartScan)
                .accessibilityHint(viewModel.uiState.isScanning ? "Stops the current Bluetooth scan." : "Starts scanning for nearby Bluetooth peripherals.")
            }

            Section("Connection") {
                Label {
                    Text(viewModel.uiState.connection.status)
                } icon: {
                    Image(systemName: statusIconName(for: viewModel.uiState.connection.status))
                }

                if let deviceName = viewModel.uiState.connection.connectedDeviceName {
                    Text("Device: \(deviceName)")
                }

                if let identifier = viewModel.uiState.connection.connectedDeviceIdentifier {
                    Text("Identifier: \(identifier)")
                        .font(.footnote)
                        .foregroundStyle(.secondary)
                }

                if viewModel.uiState.connection.connectedDeviceIdentifier != nil {
                    Button("Disconnect") {
                        viewModel.disconnect()
                    }
                    .accessibilityHint("Disconnects from the current BLE device.")
                }
            }

            Section("Discovered Devices") {
                if viewModel.uiState.devices.isEmpty {
                    Text("No BLE devices discovered yet. Start a scan to populate nearby devices.")
                        .foregroundStyle(.secondary)
                } else {
                    ForEach(viewModel.uiState.devices) { device in
                        VStack(alignment: .leading, spacing: 8) {
                            Text(device.name)
                                .font(.headline)
                            Text("Identifier: \(device.id)")
                                .font(.footnote)
                                .foregroundStyle(.secondary)
                            Text("Signal: \(device.signalLabel) (\(device.rssi) dBm)")
                                .font(.subheadline)

                            Button("Connect") {
                                viewModel.connect(to: device.id)
                            }
                            .buttonStyle(.bordered)
                            .disabled(!viewModel.uiState.canConnect || viewModel.uiState.connection.connectedDeviceIdentifier == device.id)
                            .accessibilityLabel("Connect to \(device.name)")
                        }
                        .padding(.vertical, 4)
                    }
                }
            }

            Section("Services and Characteristics") {
                if viewModel.uiState.connection.services.isEmpty {
                    Text("Connect to a device to inspect its services and readable characteristics.")
                        .foregroundStyle(.secondary)
                } else {
                    ForEach(viewModel.uiState.connection.services) { service in
                        VStack(alignment: .leading, spacing: 8) {
                            Text(service.id)
                                .font(.headline)

                            ForEach(service.characteristics) { characteristic in
                                VStack(alignment: .leading, spacing: 8) {
                                    Text(characteristic.id)
                                        .font(.subheadline)
                                    Text(propertySummary(for: characteristic))
                                        .font(.footnote)
                                        .foregroundStyle(.secondary)

                                    if let lastValue = characteristic.lastValueHex {
                                        Text("Last value: \(lastValue)")
                                            .font(.footnote)
                                    }

                                    if characteristic.isReadable {
                                        Button("Read Characteristic") {
                                            viewModel.readCharacteristic(
                                                serviceUUID: service.id,
                                                characteristicUUID: characteristic.id
                                            )
                                        }
                                        .buttonStyle(.bordered)
                                        .accessibilityLabel("Read characteristic \(characteristic.id)")
                                    }
                                }
                                .padding(.vertical, 4)
                            }
                        }
                        .padding(.vertical, 4)
                    }
                }
            }
        }
        .navigationTitle("KMP BLE Explorer")
        .listStyle(.insetGrouped)
    }

    @ViewBuilder
    private func requirementCard(
        title: String,
        description: String,
        actionTitle: String,
        action: @escaping () -> Void
    ) -> some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title)
                .font(.headline)
            Text(description)
                .font(.body)
            Button(actionTitle, action: action)
                .buttonStyle(.borderedProminent)
        }
        .padding(.vertical, 6)
    }

    private func propertySummary(for characteristic: IOSBleCharacteristic) -> String {
        let properties = [
            characteristic.isReadable ? "Readable" : "No read",
            characteristic.isWritable ? "Writable" : "Read only",
            characteristic.isNotifiable ? "Notify" : "No notify"
        ]
        return properties.joined(separator: " • ")
    }

    private func statusIconName(for status: String) -> String {
        switch status.lowercased() {
        case "connected":
            return "checkmark.circle.fill"
        case "connecting", "discovering services":
            return "dot.radiowaves.left.and.right"
        case "failed":
            return "xmark.octagon.fill"
        default:
            return "bolt.horizontal.circle"
        }
    }

    private func openSettings() {
        guard let url = URL(string: UIApplication.openSettingsURLString) else {
            return
        }
        UIApplication.shared.open(url)
    }
}
