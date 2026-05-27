# iOS App

## Open in Xcode

Open `iosApp/iosApp.xcodeproj` in Xcode.

The project is configured to:
- build the shared KMP framework with `:shared:embedAndSignAppleFrameworkForXcode`
- link the generated `SharedBle` framework
- resolve `KMPNativeCoroutinesAsync` from Swift Package Manager

## Run Requirements

- Xcode with the full iOS SDK installed
- iOS 15.0 or later
- A real iPhone for BLE scan/connect testing

The iOS Simulator can be used to verify the SwiftUI host app, navigation, and shared state rendering, but CoreBluetooth scanning and real peripheral connectivity should be tested on physical hardware.

## First Run

1. Open `iosApp/iosApp.xcodeproj`.
2. Let Xcode resolve the `KMP-NativeCoroutines` package if prompted.
3. Select the `KMPDemoiOS` scheme.
4. Run the app on a simulator to verify the host app boots.
5. Run the app on a physical device to test BLE scanning and connections.

## Bluetooth Permissions

The app includes `NSBluetoothAlwaysUsageDescription` in `iosApp/iosApp/Info.plist`.

On iOS, Bluetooth access is requested by the system when CoreBluetooth is first used. If access was denied earlier, use the in-app Settings shortcut to reopen the app's Settings page.

## If Xcode CLI Is Not Active

If `xcodebuild` points at Command Line Tools instead of the full Xcode app, switch the developer directory before using CLI builds:

```sh
sudo xcode-select -s /Applications/Xcode.app/Contents/Developer
```

If Xcode is installed in a different location, replace the path above with the correct one.
