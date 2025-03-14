package org.balhom.currencyprofilesapi.modules.transactionchanges.infrastructure.consumers.data

import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionChangeProps
import java.util.UUID

data class TransactionChangeEvent(
    val action: String,
    val id: UUID,
    val amount: Double,
    val oldAmount: Double?,
    val currencyProfileId: UUID,
    val userId: UUID,
) {
    fun toChangeProps(): TransactionChangeProps {
        return TransactionChangeProps(
            EventChangeTypeEnum.fromAction(action),
            id,
            currencyProfileId,
            userId,
            amount - (oldAmount ?: 0.0)
        )
    }
}
