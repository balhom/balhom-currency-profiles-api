package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import io.quarkus.runtime.annotations.RegisterForReflection
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.math.BigDecimal
import java.util.*

@RegisterForReflection
data class CurrencyProfileChangeEvent(
    var action: String,
    var id: UUID,
    var balance: BigDecimal,
    var monthlyGoal: BigDecimal,
    var yearlyGoal: BigDecimal,
    var ownerId: UUID,
    var sharedUsers: List<CurrencyProfileSharedUserChangeEvent>,
) {
    constructor() : this(
        "",
        UUID.randomUUID(),
        BigDecimal(0),
        BigDecimal(0),
        BigDecimal(0),
        UUID.randomUUID(),
        emptyList()
    )

    companion object {
        fun fromDomain(
            action: String,
            domain: CurrencyProfile
        ): CurrencyProfileChangeEvent = CurrencyProfileChangeEvent(
            action,
            domain.id,
            domain.balance,
            domain.goalMonthlySaving,
            domain.goalYearlySaving,
            domain.userId,
            domain
                .sharedUsers
                .map {
                    CurrencyProfileSharedUserChangeEvent(
                        it.id,
                        it.email
                    )
                }.toList(),
        )
    }
}
