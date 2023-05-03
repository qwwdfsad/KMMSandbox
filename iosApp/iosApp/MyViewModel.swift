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
    
    @Published
    var tickerErrorMessage: String = ""

    func startTicker() {
        tickerErrorMessage = ""
        cancelTicker()
        tickerTask = Task {
            cancellableTicker = tickerService.launchTickClassFlow()
                .subscribe(
                    onEach: { tick in
                        self.tickValue = tick?.intValue
                    }, onComplete: { error in
                        if let error = error {
                            self.tickValue = nil
                            self.tickerErrorMessage = "'launchTickFlow' error: \(error.message ?? "")"
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
    
    @Published
    var greetingErrorMessage: String = ""

    func loadGreeting() {
        greetingErrorMessage = ""
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
                // TODO we should check for Kotlin Cancellation exception here,
                // which is not exposed by default
                // thus, we get cancellation errors
                self.greetingErrorMessage = "'greet' call error: \(error.message ?? "")"
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
                    if let error = error {
                        self.greetingErrorMessage = "'progressFlow' error: \(error.message ?? "")"
                    }
                    self.progress = nil
                })
        }
    }

    // error mode

    func changeErrorMode(value: Bool) {
        tickerService.changeErrorMode(value: value)
        greetingService.changeErrorMode(value: value)
    }
}
