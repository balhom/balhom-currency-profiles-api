package org.balhom.currencyprofilesapi.modules.transactions.application

import jakarta.enterprise.context.ApplicationScoped
import org.balhom.currencyprofilesapi.common.data.props.ObjectIdUserProps
import org.balhom.currencyprofilesapi.modules.currencyprofiles.application.CurrencyProfileService
import org.balhom.currencyprofilesapi.modules.transactions.domain.props.TransactionChangeProps

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

        currencyProfileService.updateCurrencyProfile(
            currencyProfile
        )
    }
}
