package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import io.quarkus.runtime.annotations.RegisterForReflection
import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile
import java.math.BigDecimal
import java.util.*

@RegisterForReflection
data class CurrencyProfileChangeEvent(
    var action: String,
    var id: UUID,
    var name: String,
    var balance: BigDecimal,
    var monthlyGoal: BigDecimal,
    var yearlyGoal: BigDecimal,
    var imageUrl: String?,
    var ownerId: UUID,
    var sharedUsers: List<CurrencyProfileSharedUserChangeEvent>,
) {
    companion object {
        fun fromDomain(
            action: String,
            domain: CurrencyProfile
        ): CurrencyProfileChangeEvent = CurrencyProfileChangeEvent(
            action,
            domain.id,
            domain.name,
            domain.balance,
            domain.goalMonthlySaving,
            domain.goalYearlySaving,
            domain.imageData?.url,
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
