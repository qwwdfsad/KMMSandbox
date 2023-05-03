//
//  TickerView.swift
//  iosApp
//
//  Created by Svetlana.Isakova on 02.05.23.
//  Copyright © 2023 orgName. All rights reserved.
//

import SwiftUI

struct TickerView: View {
    let tickValue: Int?
    let tickerErrorMessage: String?
    let onStart: () -> Void
    let onCancel: () -> Void
    
    var body: some View {
        VStack {
            Text("Tick: \(tickValue?.formatted() ?? "-")")
            HStack {
                Button("Start", action: onStart)
                Button("Cancel", action: onCancel)
            }
            if let message = tickerErrorMessage {
                Text(message)
            }
        }
    }
}

struct TickerView_Previews: PreviewProvider {
    static var previews: some View {
        TickerView(
            tickValue: 10,
            tickerErrorMessage: nil,
            onStart: {},
            onCancel: {}
        )
    }
}
