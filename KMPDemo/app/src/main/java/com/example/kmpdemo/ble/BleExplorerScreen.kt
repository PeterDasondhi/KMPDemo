package com.example.kmpdemo.ble

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kmpdemo.R
import com.example.kmpdemo.shared.ble.BleCharacteristicInfo
import com.example.kmpdemo.shared.ble.BleConnectionStatus
import com.example.kmpdemo.shared.ble.BleDevice
import com.example.kmpdemo.shared.ble.BleExplorerUiState
import com.example.kmpdemo.shared.ble.BleServiceInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BleExplorerScreen(
    uiState: BleExplorerUiState,
    onToggleScan: () -> Unit,
    onConnect: (String) -> Unit,
    onDisconnect: () -> Unit,
    onReadCharacteristic: (String, String) -> Unit,
    onRequestPermissions: () -> Unit,
    onEnableBluetooth: () -> Unit,
    onDismissMessage: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.kmp_ble_explorer)) },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (!uiState.requirements.hasBluetoothPermissions) {
                item {
                    RequirementCard(
                        title = "Bluetooth permissions required",
                        description = "Grant scan and connect permissions before searching for BLE devices.",
                        actionLabel = "Grant permissions",
                        onAction = onRequestPermissions,
                    )
                }
            }

            if (!uiState.requirements.isBluetoothEnabled) {
                item {
                    RequirementCard(
                        title = "Bluetooth is turned off",
                        description = "Enable Bluetooth to discover and connect to nearby devices.",
                        actionLabel = "Enable Bluetooth",
                        onAction = onEnableBluetooth,
                    )
                }
            }

            uiState.bannerMessage?.let { message ->
                item {
                    MessageCard(
                        message = message,
                        onDismiss = onDismissMessage,
                    )
                }
            }

            item {
                ScanControlsCard(
                    isScanning = uiState.isScanning,
                    canStartScan = uiState.canStartScan,
                    onToggleScan = onToggleScan,
                )
            }

            item {
                ConnectionCard(
                    uiState = uiState,
                    onDisconnect = onDisconnect,
                )
            }

            item {
                SectionTitle(title = "Discovered devices")
            }

            if (uiState.devices.isEmpty()) {
                item {
                    EmptyStateCard(
                        message = "No BLE devices discovered yet. Start a scan to populate nearby devices.",
                    )
                }
            } else {
                items(
                    items = uiState.devices,
                    key = { device -> device.address },
                ) { device ->
                    DeviceCard(
                        device = device,
                        canConnect = uiState.canConnect,
                        connectionState = uiState.connectionState.status,
                        connectedAddress = uiState.connectionState.connectedDeviceAddress,
                        onConnect = onConnect,
                    )
                }
            }

            item {
                SectionTitle(title = "Services and characteristics")
            }

            if (uiState.connectionState.services.isEmpty()) {
                item {
                    EmptyStateCard(
                        message = "Connect to a device to inspect discovered services and readable characteristics.",
                    )
                }
            } else {
                items(
                    items = uiState.connectionState.services,
                    key = { service -> service.uuid },
                ) { service ->
                    ServiceCard(
                        service = service,
                        onReadCharacteristic = onReadCharacteristic,
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
private fun RequirementCard(
    title: String,
    description: String,
    actionLabel: String,
    onAction: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = description, style = MaterialTheme.typography.bodyMedium)
            Button(
                onClick = onAction,
                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp),
            ) {
                Text(actionLabel)
            }
        }
    }
}

@Composable
private fun MessageCard(
    message: String,
    onDismiss: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
            )
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp),
            ) {
                Text("Dismiss")
            }
        }
    }
}

@Composable
private fun ScanControlsCard(
    isScanning: Boolean,
    canStartScan: Boolean,
    onToggleScan: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Scan controls",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = if (isScanning) {
                    "Scanning for nearby BLE peripherals."
                } else {
                    "Start a fresh scan to discover nearby devices."
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(
                onClick = onToggleScan,
                enabled = isScanning || canStartScan,
                modifier = Modifier
                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .semantics {
                        role = Role.Button
                        stateDescription = if (isScanning) {
                            "Scanning in progress"
                        } else {
                            "Scan stopped"
                        }
                    },
            ) {
                Text(if (isScanning) "Stop scan" else "Start scan")
            }
        }
    }
}

@Composable
private fun ConnectionCard(
    uiState: BleExplorerUiState,
    onDisconnect: () -> Unit,
) {
    val connectionState = uiState.connectionState
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                liveRegion = LiveRegionMode.Polite
                stateDescription = "Connection ${connectionState.status.toAccessibleStatusText()}"
            },
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Connection",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            StatusRow(
                status = connectionState.status,
            )
            connectionState.connectedDeviceName?.let { deviceName ->
                Text("Device: $deviceName", style = MaterialTheme.typography.bodyMedium)
            }
            connectionState.connectedDeviceAddress?.let { address ->
                Text("Address: $address", style = MaterialTheme.typography.bodySmall)
            }
            if (connectionState.status != BleConnectionStatus.DISCONNECTED) {
                OutlinedButton(
                    onClick = onDisconnect,
                    modifier = Modifier.sizeIn(minWidth = 48.dp, minHeight = 48.dp),
                ) {
                    Text("Disconnect")
                }
            }
        }
    }
}

@Composable
private fun DeviceCard(
    device: BleDevice,
    canConnect: Boolean,
    connectionState: BleConnectionStatus,
    connectedAddress: String?,
    onConnect: (String) -> Unit,
) {
    val isConnectedDevice = device.address == connectedAddress
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .semantics(mergeDescendants = false) {
                    stateDescription = buildString {
                        append(device.accessibleName())
                        append(", signal ${device.signalStrengthLabel()}")
                        if (isConnectedDevice) {
                            append(", ${connectionState.toAccessibleStatusText()}")
                        }
                    }
                },
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = device.name ?: "Unnamed device",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(text = device.address, style = MaterialTheme.typography.bodySmall)
            Text(
                text = "Signal ${device.signalStrengthLabel()} (${device.rssi} dBm)",
                style = MaterialTheme.typography.bodySmall,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isConnectedDevice && connectionState == BleConnectionStatus.CONNECTED) {
                    StatusBadge(label = "Connected", color = MaterialTheme.colorScheme.primary)
                } else if (isConnectedDevice && connectionState == BleConnectionStatus.CONNECTING) {
                    StatusBadge(label = "Connecting", color = MaterialTheme.colorScheme.tertiary)
                }

                Button(
                    onClick = { onConnect(device.address) },
                    enabled = canConnect && !isConnectedDevice,
                    modifier = Modifier
                        .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                        .semantics {
                            role = Role.Button
                            contentDescription = "Connect to ${device.accessibleName()}"
                        },
                ) {
                    Text("Connect")
                }
            }
        }
    }
}

@Composable
private fun ServiceCard(
    service: BleServiceInfo,
    onReadCharacteristic: (String, String) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Service ${service.uuid}",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )
            service.characteristics.forEachIndexed { index, characteristic ->
                CharacteristicRow(
                    serviceUuid = service.uuid,
                    characteristic = characteristic,
                    onReadCharacteristic = onReadCharacteristic,
                )
                if (index < service.characteristics.lastIndex) Divider()
            }
        }
    }
}

@Composable
private fun CharacteristicRow(
    serviceUuid: String,
    characteristic: BleCharacteristicInfo,
    onReadCharacteristic: (String, String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Text(
            text = characteristic.uuid,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PropertyBadge(label = if (characteristic.properties.isReadable) "Readable" else "No read")
            PropertyBadge(label = if (characteristic.properties.isWritable) "Writable" else "Read-only")
            PropertyBadge(label = if (characteristic.properties.isNotifiable) "Notify" else "No notify")
        }
        characteristic.lastValueHex?.let { value ->
            Surface(
                shape = MaterialTheme.shapes.small,
                tonalElevation = 2.dp,
            ) {
                Text(
                    text = "Last value: $value",
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
        if (characteristic.properties.isReadable) {
            TextButton(
                onClick = {
                    onReadCharacteristic(
                        serviceUuid,
                        characteristic.uuid,
                    )
                },
                modifier = Modifier
                    .sizeIn(minWidth = 48.dp, minHeight = 48.dp)
                    .semantics {
                        role = Role.Button
                        contentDescription = "Read characteristic ${characteristic.uuid}"
                    },
            ) {
                Text("Read characteristic")
            }
        }
    }
}

@Composable
private fun PropertyBadge(label: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

@Composable
private fun EmptyStateCard(message: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        modifier = Modifier.semantics { heading() },
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun StatusRow(status: BleConnectionStatus) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(
                    color = status.color(),
                    shape = CircleShape,
                ),
        )
        Text(
            text = "Status: ${status.toDisplayName()}",
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun StatusBadge(
    label: String,
    color: Color,
) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = 0.16f),
        contentColor = color,
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

private fun BleConnectionStatus.toDisplayName(): String {
    return name
        .lowercase()
        .replace('_', ' ')
        .replaceFirstChar { character -> character.titlecase() }
}

private fun BleConnectionStatus.toAccessibleStatusText(): String {
    return when (this) {
        BleConnectionStatus.DISCONNECTED -> "disconnected"
        BleConnectionStatus.CONNECTING -> "connecting"
        BleConnectionStatus.DISCOVERING_SERVICES -> "discovering services"
        BleConnectionStatus.CONNECTED -> "connected"
        BleConnectionStatus.FAILED -> "connection failed"
    }
}

@Composable
private fun BleConnectionStatus.color(): Color {
    return when (this) {
        BleConnectionStatus.DISCONNECTED -> MaterialTheme.colorScheme.outline
        BleConnectionStatus.CONNECTING -> MaterialTheme.colorScheme.tertiary
        BleConnectionStatus.DISCOVERING_SERVICES -> MaterialTheme.colorScheme.secondary
        BleConnectionStatus.CONNECTED -> MaterialTheme.colorScheme.primary
        BleConnectionStatus.FAILED -> MaterialTheme.colorScheme.error
    }
}

private fun BleDevice.accessibleName(): String {
    return name ?: "Unnamed device ${address.takeLast(5)}"
}

private fun BleDevice.signalStrengthLabel(): String {
    return when {
        rssi >= -55 -> "strong"
        rssi >= -70 -> "medium"
        else -> "weak"
    }
}
