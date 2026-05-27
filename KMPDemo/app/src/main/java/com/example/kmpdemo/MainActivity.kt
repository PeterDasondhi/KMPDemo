package com.example.kmpdemo

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kmpdemo.ble.BleExplorerScreen
import com.example.kmpdemo.ble.BleExplorerViewModel
import com.example.kmpdemo.ble.BluetoothPermissionHelper
import com.example.kmpdemo.shared.ble.BleConnectionStatus
import com.example.kmpdemo.ui.theme.KMPDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KMPDemoTheme {
                BleExplorerRoute()
            }
        }
    }
}

@Composable
private fun BleExplorerRoute(
    bleViewModel: BleExplorerViewModel = viewModel(),
) {
    val context = LocalContext.current
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val uiState by bleViewModel.uiState.collectAsStateWithLifecycle()

    val refreshSystemState = {
        bleViewModel.updateSystemState(
            hasBluetoothPermissions = BluetoothPermissionHelper.hasRequiredPermissions(context),
            isBluetoothEnabled = BluetoothPermissionHelper.isBluetoothEnabled(context),
        )
    }

    val permissionsLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        refreshSystemState()
    }

    val bluetoothLauncher = androidx.activity.compose.rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) {
        refreshSystemState()
    }

    LaunchedEffect(Unit) {
        refreshSystemState()
    }

    LaunchedEffect(uiState.connectionState.status) {
        if (uiState.connectionState.status == BleConnectionStatus.CONNECTED) {
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) refreshSystemState()
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BleExplorerScreen(
        uiState = uiState,
        onToggleScan = bleViewModel::toggleScan,
        onConnect = bleViewModel::connect,
        onDisconnect = bleViewModel::disconnect,
        onReadCharacteristic = bleViewModel::readCharacteristic,
        onRequestPermissions = {
            permissionsLauncher.launch(BluetoothPermissionHelper.requiredPermissions())
        },
        onEnableBluetooth = {
            bluetoothLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        },
        onDismissMessage = bleViewModel::clearMessage,
    )
}