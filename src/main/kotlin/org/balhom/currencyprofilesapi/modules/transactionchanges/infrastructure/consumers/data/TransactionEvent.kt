package org.balhom.currencyprofilesapi.modules.transactionchanges.infrastructure.consumers.data

import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionChangeProps
import java.util.UUID

data class TransactionEvent(
    val action: String,
    val id: UUID,
    val balance: Double,
    val oldBalance: Double?,
    val currencyProfileId: UUID,
    val userId: UUID,
) {
    fun toChangeProps(): TransactionChangeProps {
        return TransactionChangeProps(
            EventChangeTypeEnum.fromAction(action),
            id,
            currencyProfileId,
            userId,
            balance - (oldBalance ?: 0.0)
        )
    }
}
