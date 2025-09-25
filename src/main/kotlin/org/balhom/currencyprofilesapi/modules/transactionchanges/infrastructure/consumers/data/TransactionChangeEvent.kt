package org.balhom.currencyprofilesapi.modules.transactionchanges.infrastructure.consumers.data

import io.quarkus.runtime.annotations.RegisterForReflection
import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.enums.TransactionTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionChangeProps
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionOldDataProps
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@RegisterForReflection
data class TransactionChangeEvent(
    var action: String,
    var id: UUID,
    var type: TransactionTypeEnum,
    var date: LocalDateTime,
    var oldDate: LocalDateTime?,
    var amount: BigDecimal,
    var oldAmount: BigDecimal?,
    var currencyProfileId: UUID,
    var userId: UUID,
) {
    fun toChangeProps(): TransactionChangeProps {
        return TransactionChangeProps(
            eventChangeType = EventChangeTypeEnum
                .fromAction(action),
            id = id,
            type = type,
            date = date,
            amount = amount,
            oldData = if (
                oldDate == null
                || oldAmount == null
            ) null else TransactionOldDataProps(
                oldDate = oldDate !!,
                oldAmount = oldAmount !!
            ),
            currencyProfileId = currencyProfileId,
            userId = userId,
        )
    }
}
