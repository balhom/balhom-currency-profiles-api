package org.balhom.currencyprofilesapi.modules.transactions.domain.props

import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import java.util.UUID

data class TransactionChangeProps(
    val eventChangeTypeEnum: EventChangeTypeEnum,
    val transactionId: UUID,
    val currencyProfileId: UUID,
    val userId: UUID,
    val sum: Double,
)
