package org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props

import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import java.math.BigDecimal
import java.util.UUID

data class TransactionChangeProps(
    val eventChangeType: EventChangeTypeEnum,
    val id: UUID,
    val sum: BigDecimal,
    val currencyProfileId: UUID,
    val userId: UUID,
)
