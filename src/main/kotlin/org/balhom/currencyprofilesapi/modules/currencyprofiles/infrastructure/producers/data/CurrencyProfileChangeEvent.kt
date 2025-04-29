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

    fun toMap(): Map<String, Any> = mapOf(
        "action" to action,
        "id" to id.toString(),
        "balance" to balance.toString(),
        "monthlyGoal" to monthlyGoal.toString(),
        "yearlyGoal" to yearlyGoal.toString(),
        "ownerId" to ownerId.toString(),
        "sharedUsers" to sharedUsers
            .map {
                mapOf(
                    "id" to it.id.toString(),
                    "email" to it.email
                )
            },
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
