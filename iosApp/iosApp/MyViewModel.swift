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
    
    func startTicker() {
        cancelTicker()
        tickerTask = Task {
            do {
                let sequence = asyncSequence(for: tickerService.launchTickFlow())
                for try await tick in sequence {
                    tickValue = tick.intValue
                }
            } catch {
                tickValue = nil
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
    
    func loadGreeting() {
        cancelGreeting()
        greetingTask = Task {
            greetingText = "Loading..."
            let result = await asyncResult(for: greetingService.greet())
            if case let .success(text) = result {
                greetingText = text
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
                greetingText = "Failed with error: \(error)"
                progress = nil
            }
        }
    }
}
