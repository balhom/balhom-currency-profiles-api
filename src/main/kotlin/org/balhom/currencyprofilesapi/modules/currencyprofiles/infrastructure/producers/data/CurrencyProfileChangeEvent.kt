package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfileSharedUser
import java.math.BigDecimal
import java.util.UUID

data class CurrencyProfileChangeEvent(
    val action: String,
    val id: UUID,
    val balance: BigDecimal,
    val monthlyGoal: BigDecimal,
    val yearlyGoal: BigDecimal,
    val ownerId: UUID,
    val sharedUsers: MutableList<CurrencyProfileSharedUser>,
)
