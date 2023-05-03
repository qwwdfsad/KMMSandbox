import SwiftUI
import shared

struct ContentView: View {
    @StateObject
    var myViewModel: MyViewModel
        
    var body: some View {
        VStack {
            TickerView(
                tickValue: myViewModel.tickValue,
                tickerErrorMessage: myViewModel.tickerErrorMessage,
                onStart: { myViewModel.startTicker() },
                onCancel: { myViewModel.cancelTicker() }
            )

            Divider()
            
            GreetingWithProgressView(
                progress: myViewModel.progress,
                greetingText: myViewModel.greetingText,
                greetingErrorMessage: myViewModel.greetingErrorMessage,
                onLoad: { myViewModel.loadGreeting() },
                onCancel: { myViewModel.cancelGreeting() }
            )
            
            Divider()
            
            ErrorModeView(
                onChange: { value in myViewModel.changeErrorMode(value:value) }
            )
        }
    }
}

struct ErrorModeView: View {
    @State var isOn: Bool = false
    let onChange: (Bool) -> Void
    
    var body: some View {
        Toggle(isOn: $isOn) {
           Text("Error mode")
        }
        .padding()
        .onChange(of: isOn) { newValue in
            onChange(newValue)
        }
    }
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView(myViewModel: MyViewModel())
	}
}
