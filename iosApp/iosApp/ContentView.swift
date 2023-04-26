import SwiftUI
import shared

struct ContentView: View {
    @StateObject
    var myViewModel: MyViewModel
        
    var body: some View {
        VStack {
            TickerView(
                tickValue: myViewModel.tickValue,
                onStart: { myViewModel.startTicker() },
                onCancel: { myViewModel.cancelTicker() }
            )

            Divider()
            
            GreetingWithProgressView(
                progress: myViewModel.progress,
                greetingText: myViewModel.greetingText,
                onLoad: { myViewModel.loadGreeting() },
                onCancel: { myViewModel.cancelGreeting() }
            )
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView(myViewModel: MyViewModel())
	}
}
