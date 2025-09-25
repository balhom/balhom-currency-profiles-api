package org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props

import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomBigDecimal
import org.balhom.currencyprofilesapi.common.utils.TestDataUtils.Companion.randomPastDateTime
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.enums.TransactionTypeEnum
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class MockTransactionChangePropsFactory {
    companion object {
        fun create(
            eventChangeType: EventChangeTypeEnum,
            type: TransactionTypeEnum = TransactionTypeEnum.entries.random(),
            amount: BigDecimal = randomBigDecimal(1.0, 1000.0),
            currencyProfileId: UUID = UUID.randomUUID(),
            userId: UUID = UUID.randomUUID(),
        ): TransactionChangeProps {

            val oldDate: LocalDateTime? = if (
                eventChangeType == EventChangeTypeEnum.UPDATE
            ) randomPastDateTime() else null

            val oldAmount: BigDecimal? = if (
                eventChangeType == EventChangeTypeEnum.UPDATE
            ) randomBigDecimal(1.0, 1000.0) else null

            return TransactionChangeProps(
                eventChangeType = eventChangeType,
                id = UUID.randomUUID(),
                type = type,
                date = randomPastDateTime(),
                amount = amount,
                oldData = if (
                    oldDate == null
                    || oldAmount == null
                ) null else TransactionOldDataProps(
                    oldDate = oldDate,
                    oldAmount = oldAmount
                ),
                currencyProfileId = currencyProfileId,
                userId = userId
            )
        }
    }
}
