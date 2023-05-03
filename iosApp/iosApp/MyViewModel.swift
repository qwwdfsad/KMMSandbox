//
//  GreetingViewModel.swift
//  iosApp
//
//  Created by Svetlana.Isakova on 26.04.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared
import KMPNativeCoroutinesCore
import KMPNativeCoroutinesAsync
import KMPNativeCoroutinesCombine

@MainActor
class MyViewModel: ObservableObject {
    
    // flow
    
    private let tickerService = TickerService()
    private var tickerTask: Task<(), Never>? = nil
    
    @Published
    var tickValue: Int? = nil
    
    @Published
    var tickerErrorMessage: String = ""
    
    func startTicker() {
        tickerErrorMessage = ""
        cancelTicker()
        tickerTask = Task {
            do {
                let sequence = asyncSequence(for: tickerService.launchTickFlow())
                for try await tick in sequence {
                    tickValue = tick.intValue
                }
            } catch {
                tickValue = nil
                tickerErrorMessage = "'launchTickFlow' error: \(error.localizedDescription)"
            }
        }
    }
    
    func cancelTicker() {
        tickerTask?.cancel()
        tickValue = nil
    }
    
    
    // suspend function
    
    private let greetingService = GreetingService()
    
    private var greetingTask: Task<(), Never>? = nil
    
    @Published
    var greetingText: String = ""
    
    @Published
    var greetingErrorMessage: String = ""
    
    func loadGreeting() {
        greetingErrorMessage = ""
        cancelGreeting()
        greetingTask = Task {
            greetingText = "Loading..."
            // alternative: to use do-catch and
            // try await asyncFunction(for: greetingService.greet())
            
            let result = await asyncResult(for: greetingService.greet())
            switch result {
            case let.success(text):
                greetingText = text
            case let.failure(error):
                if !(error is CancellationError)  {
                    greetingErrorMessage = "'greet' call error: \(error.localizedDescription)"
                }
            }
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
            do {
                let sequence = asyncSequence(for: greetingService.progressFlow)
                for try await value in sequence {
                    progress = value
                }
            } catch {
                greetingErrorMessage = "'progressFlow' error: \(error.localizedDescription)"
                progress = nil
            }
        }
    }
    
    // error mode
    
    func changeErrorMode(value: Bool) {
        tickerService.changeErrorMode(value: value)
        greetingService.changeErrorMode(value: value)
    }
}
