package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.producers.data

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfileSharedUser
import java.math.BigDecimal
import java.util.*

data class CurrencyProfileChangeEvent(
    val action: String,
    val id: UUID,
    val balance: BigDecimal,
    val monthlyGoal: BigDecimal,
    val yearlyGoal: BigDecimal,
    val ownerId: UUID,
    val sharedUsers: List<CurrencyProfileSharedUser>,
) {
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
}
