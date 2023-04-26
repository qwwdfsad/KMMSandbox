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
    private var cancellableTicker: Cancellable? = nil
    
    @Published
    var tickValue: Int? = nil
    
    func startTicker() {
        cancelTicker()
        tickerTask = Task {
            cancellableTicker = tickerService.launchTickClassFlow()
                .subscribe(
                    onEach: { tick in
                        self.tickValue = tick?.intValue
                    }, onComplete: { error in
                        if error != nil {
                            self.tickValue = nil
                        }
                    })
        }
    }
    
    func cancelTicker() {
        cancellableTicker?.cancel()
        tickerTask?.cancel()
        tickValue = nil
    }
    
    
    // suspend function
    
    private let greetingService = GreetingService()
    
    private var greetingTask: Task<(), Error>? = nil
    private var cancellableGreeting: Cancellable? = nil
    
    @Published
    var greetingText: String = ""
    
    func loadGreeting() {
        cancelGreeting()
        greetingTask = Task {
            greetingText = "Loading..."
            // in this way, cancellation is not supported:
//            greetingText = try await greetingService.greet()
            
            // we need to add an explicit cancellable wrapper:
            cancellableGreeting = greetingService.greetWrapper().subscribe(onSuccess: { value in
                if let value = value {
                    self.greetingText = String(value)
                }
            }, onThrow: { error in
                // we need to somehow filter out Kotlin CancellationException (which is not exposed by default)
                // otherwise this line gets print with CancellationException:
                // self.greetingText = "Error occurred: \(error)"
            })
        }
    }

    func cancelGreeting() {
        greetingText = "Loading canceled"
        cancellableGreeting?.cancel()
        greetingTask?.cancel()
    }

    @Published
    var progress: String? = nil
    
    init() {
        Task {
            greetingService.progressFlow.subscribe(
                onEach: { value in
                    if (value == nil) {
                        self.progress = nil
                    } else {
                        self.progress = String(value ?? "")
                    }
                    
                },
                onComplete: { error in
                    if (error != nil) {
                        self.greetingText = "Failed with error: \(error!)"
                    }
                    self.progress = nil
                })
        }
    }
}
