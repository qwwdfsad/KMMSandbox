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
    
    @Published
    var tickerErrorMessage: String = ""

    func startTicker() {
        tickerErrorMessage = ""
        cancelTicker()
        tickerTask = Task {
            do {
                let tickAsyncSequence: SkieSwiftFlow<KotlinInt> = tickerService.launchTickFlow()
                for try await tick in tickAsyncSequence {
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
    
    private var greetingTask: Task<(), Error>? = nil
    
    @Published
    var greetingText: String = ""
    
    @Published
    var greetingErrorMessage: String = ""

    func loadGreeting() {
        greetingErrorMessage = ""
        cancelGreeting()
        greetingTask = Task {
            greetingText = "Loading..."
            do {
                greetingText = try await greetingService.greet()
            } catch {
                //if !(error is CancellationError)  {
                    greetingErrorMessage = "'greet' call error: \(error.localizedDescription)"
                //}
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
            let progressAsyncSequence: SkieSwiftOptionalFlow<String> =
                greetingService.progressFlow
            for await it in progressAsyncSequence {
                progress = it
            }
        }
    }

    // error mode

    func changeErrorMode(value: Bool) {
        tickerService.changeErrorMode(value: value)
        greetingService.changeErrorMode(value: value)
    }
}
