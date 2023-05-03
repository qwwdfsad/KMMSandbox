//
//  GreetingWithCountdownView.swift
//  iosApp
//
//  Created by Svetlana.Isakova on 28.04.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct GreetingWithProgressView: View {
    let progress: String?
    let greetingText: String?
    let greetingErrorMessage: String?
    let onLoad: () -> Void
    let onCancel: () -> Void
    
    var body: some View {
        VStack {
            Text(greetingText ?? "")
            Text(loadingText())
            HStack {
                Button("Load", action: onLoad)
                Button("Cancel", action: onCancel)
            }
            if let message = greetingErrorMessage {
                Text(message)
            }
        }
    }
    
    private func loadingText() -> String {
        if progress == nil {
            return "No loading in progress"
        }
        return "Loading progress: \(progress ?? "")"
    }
}

struct GreetingWithCountdownView_Previews: PreviewProvider {
    static var previews: some View {
        GreetingWithProgressView(
            progress: "",
            greetingText: "Hello",
            greetingErrorMessage: nil,
            onLoad: {},
            onCancel: {}
        )
    }
}
