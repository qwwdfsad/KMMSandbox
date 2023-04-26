import SwiftUI

@main
struct iOSApp: App {
	var body: some Scene {
        let myViewModel = MyViewModel()
		WindowGroup {
            ContentView(myViewModel: myViewModel)
		}
	}
}
