# KMPDemo

Kotlin Multiplatform sample for **Bluetooth Low Energy (BLE)** on Android and iOS.

## What it does

- Scan for nearby BLE peripherals  
- Connect / disconnect  
- Discover services and characteristics  
- Read readable characteristics  

Shared state and flows live in the `:shared` module; Android uses Jetpack Compose + `BluetoothGatt`, iOS uses SwiftUI + CoreBluetooth (Kotlin/Native).

## Structure

| Module | Role |
|--------|------|
| `shared` | Common BLE models, presenter, Android/iOS `BleController` |
| `app` | Android Compose UI |
| `iosApp` | SwiftUI host + Xcode project |

## Requirements

- **Android:** API 24+, BLE-capable device/emulator  
- **iOS:** Xcode, iOS 15+, real device recommended for BLE  