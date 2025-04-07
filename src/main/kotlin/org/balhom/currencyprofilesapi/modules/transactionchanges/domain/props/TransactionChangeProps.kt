package org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props

import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import java.math.BigDecimal
import java.util.*

data class TransactionChangeProps(
    val eventChangeTypeEnum: EventChangeTypeEnum,
    val transactionId: UUID,
    val currencyProfileId: UUID,
    val userId: UUID,
    val sum: BigDecimal,
)
