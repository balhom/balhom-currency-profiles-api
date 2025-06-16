package org.balhom.currencyprofilesapi.modules.transactionchanges.infrastructure.consumers.data

import io.quarkus.runtime.annotations.RegisterForReflection
import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.enums.TransactionTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionChangeProps
import java.math.BigDecimal
import java.util.*

@RegisterForReflection
data class TransactionChangeEvent(
    var action: String,
    var id: UUID,
    var type: TransactionTypeEnum,
    var amount: BigDecimal,
    var oldAmount: BigDecimal?,
    var currencyProfileId: UUID,
    var userId: UUID,
) {
    fun toChangeProps(): TransactionChangeProps {
        val sum = (if (type == TransactionTypeEnum.INCOME) BigDecimal(1) else BigDecimal(-1)) *
                (amount - (oldAmount ?: BigDecimal(0.0)))

        return TransactionChangeProps(
            EventChangeTypeEnum.fromAction(action),
            id,
            currencyProfileId,
            userId,
            sum
        )
    }
}
