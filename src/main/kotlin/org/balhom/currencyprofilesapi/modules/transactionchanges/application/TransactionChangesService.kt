package org.balhom.currencyprofilesapi.modules.transactionchanges.application

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.data.enums.EventChangeTypeEnum
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileService
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.enums.TransactionTypeEnum
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionChangeProps
import java.math.BigDecimal

@ApplicationScoped
class TransactionChangesService(
    private val currencyProfileService: CurrencyProfileService
) {

    fun processChange(props: TransactionChangeProps) {
        // Old data can only be filled for update events
        if (props.eventChangeType != EventChangeTypeEnum.UPDATE) {
            props.oldData = null
        }

        var sum: BigDecimal = props.amount - (
                props.oldData?.oldAmount ?: BigDecimal(0.0)
                )

        if (props.type == TransactionTypeEnum.EXPENSE) {
            sum = sum.negate()
        }

        if (props.eventChangeType == EventChangeTypeEnum.DELETE) {
            sum = sum.negate()
        }

        val currencyProfile = currencyProfileService.getCurrencyProfile(
            ObjectIdUserProps(
                props.currencyProfileId,
                props.userId
            )
        )

        currencyProfile.balance += sum

        currencyProfileService.internalUpdateCurrencyProfile(
            currencyProfile
        )
    }
}
