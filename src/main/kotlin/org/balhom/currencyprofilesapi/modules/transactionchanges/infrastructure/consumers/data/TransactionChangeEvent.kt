package org.balhom.currencyprofilesapi.modules.transactionchanges.infrastructure.consumers.data

import io.quarkus.runtime.annotations.RegisterForReflection
import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionChangeProps
import java.math.BigDecimal
import java.util.*

@RegisterForReflection
data class TransactionChangeEvent(
    val action: String,
    val id: UUID,
    val amount: BigDecimal,
    val oldAmount: BigDecimal?,
    val currencyProfileId: UUID,
    val userId: UUID,
) {
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
