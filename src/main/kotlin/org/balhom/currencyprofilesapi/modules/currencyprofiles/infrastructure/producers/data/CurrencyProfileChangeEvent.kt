package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import io.quarkus.runtime.annotations.RegisterForReflection
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfileSharedUser
import java.math.BigDecimal
import java.util.UUID

@RegisterForReflection
data class CurrencyProfileChangeEvent(
    val action: String,
    val id: UUID,
    val balance: BigDecimal,
    val monthlyGoal: BigDecimal,
    val yearlyGoal: BigDecimal,
    val ownerId: UUID,
    val sharedUsers: MutableList<CurrencyProfileSharedUser>,
)
