package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import java.util.UUID

data class CurrencyProfileEvent(
    val action: String,
    val id: UUID,
    var balance: Double,
    val monthlyGoal: Double,
    var yearlyGoal: Double,
    val userId: UUID,
)
