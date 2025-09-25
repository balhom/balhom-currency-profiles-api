package org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props

import java.math.BigDecimal
import java.time.LocalDateTime

data class TransactionOldDataProps(
    var oldDate: LocalDateTime,
    var oldAmount: BigDecimal,
)
