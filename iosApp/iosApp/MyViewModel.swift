//
//  GreetingViewModel.swift
//  iosApp
//
//  Created by Svetlana.Isakova on 26.04.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor
class MyViewModel: ObservableObject {
    
    // flow
    
    private let tickerService = TickerService()
    private var tickerTask: Task<(), Never>? = nil
    
    @Published
    var tickValue: Int? = nil
    
    func startTicker() {
        cancelTicker()
        tickerTask = Task {
            let tickAsyncSequence: SkieSwiftFlow<KotlinInt> = tickerService.launchTickFlow()
            for await it in tickAsyncSequence {
                tickValue = it.intValue
            }
        }
    }
    
    func cancelTicker() {
        tickerTask?.cancel()
        tickValue = nil
    }
    
    
    // suspend function
    
    private let greetingService = GreetingService()
    
    private var greetingTask: Task<(), Error>? = nil
    
    @Published
    var greetingText: String = ""
    
    func loadGreeting() {
        cancelGreeting()
        greetingTask = Task {
            greetingText = "Loading..."
            // with skie, cancellation is supported:
            greetingText = try await greetingService.greet()
        }
    }

    func cancelGreeting() {
        greetingText = "Loading canceled"
        greetingTask?.cancel()
    }

    @Published
    var progress: String? = nil
    
    init() {
        Task {
            let progressAsyncSequence: SkieSwiftOptionalFlow<String> =
                greetingService.progressFlow
            for await it in progressAsyncSequence {
                progress = it
            }
        }
    }
}
