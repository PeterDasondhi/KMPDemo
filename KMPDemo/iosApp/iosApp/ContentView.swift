import SwiftUI

struct ContentView: View {
    @Environment(\.scenePhase) private var scenePhase
    @StateObject private var viewModel = IOSBleExplorerViewModel()

    var body: some View {
        NavigationView {
            BleExplorerView(viewModel: viewModel)
        }
        .navigationViewStyle(.stack)
        .onAppear {
            viewModel.refreshBluetoothState()
        }
        .onChange(of: scenePhase) { newPhase in
            if newPhase == .active {
                viewModel.refreshBluetoothState()
            }
        }
    }
}

#Preview {
    ContentView()
}
