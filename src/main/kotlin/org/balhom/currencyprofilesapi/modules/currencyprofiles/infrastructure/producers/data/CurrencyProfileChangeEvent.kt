package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfileSharedUser
import java.util.UUID

data class CurrencyProfileChangeEvent(
    val action: String,
    val id: UUID,
    val balance: Double,
    val monthlyGoal: Double,
    val yearlyGoal: Double,
    val ownerId: UUID,
    val sharedUsers: MutableList<CurrencyProfileSharedUser>,
)
