package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import java.util.UUID

data class CurrencyProfileEvent(
    val action: String,
    val id: UUID,
    val monthlyGoal: Double,
    var yearlyGoal: Double
)
