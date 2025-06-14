package org.balhom.currencyprofilesapi.modules.transactionchanges.application

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileService
import org.balhom.currencyprofilesapi.modules.transactionchanges.domain.props.TransactionChangeProps

@ApplicationScoped
class TransactionChangesService(
    private val currencyProfileService: CurrencyProfileService
) {

    fun processChange(props: TransactionChangeProps) {
        val currencyProfile = currencyProfileService.getCurrencyProfile(
            ObjectIdUserProps(
                props.currencyProfileId,
                props.userId
            )
        )

        currencyProfile.balance += props.sum

        currencyProfileService.internalUpdateCurrencyProfile(
            currencyProfile
        )
    }
}
