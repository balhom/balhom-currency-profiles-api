package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfileSharedUser
import java.util.UUID

data class CurrencyProfileChangeEvent(
    val action: String,
    val id: UUID,
    var balance: Double,
    val monthlyGoal: Double,
    var yearlyGoal: Double,
    val ownerId: UUID,
    var sharedUsers: MutableList<CurrencyProfileSharedUser>,
)
