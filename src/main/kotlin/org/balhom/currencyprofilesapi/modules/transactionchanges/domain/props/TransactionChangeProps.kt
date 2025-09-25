package org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props

import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.enums.TransactionTypeEnum
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class TransactionChangeProps(
    var eventChangeType: EventChangeTypeEnum,
    var id: UUID,
    var type: TransactionTypeEnum,
    var date: LocalDateTime,
    var amount: BigDecimal,
    var oldData: TransactionOldDataProps?,
    var currencyProfileId: UUID,
    var userId: UUID,
)
