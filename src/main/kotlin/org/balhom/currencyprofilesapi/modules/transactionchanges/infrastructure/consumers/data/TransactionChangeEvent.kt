package org.balhom.currencyprofilesapi.modules.transactionchanges.infrastructure.consumers.data

import io.quarkus.runtime.annotations.RegisterForReflection
import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionChangeProps
import java.math.BigDecimal
import java.util.*

@RegisterForReflection
data class TransactionChangeEvent(
    var action: String,
    var id: UUID,
    var amount: BigDecimal,
    var oldAmount: BigDecimal?,
    var currencyProfileId: UUID,
    var userId: UUID,
) {
    constructor() : this(
        "",
        UUID.randomUUID(),
        BigDecimal(0),
        null,
        UUID.randomUUID(),
        UUID.randomUUID(),
    )

    fun toChangeProps(): TransactionChangeProps {
        return TransactionChangeProps(
            EventChangeTypeEnum.fromAction(action),
            id,
            currencyProfileId,
            userId,
            amount - (oldAmount ?: BigDecimal(0.0))
        )
    }
}
