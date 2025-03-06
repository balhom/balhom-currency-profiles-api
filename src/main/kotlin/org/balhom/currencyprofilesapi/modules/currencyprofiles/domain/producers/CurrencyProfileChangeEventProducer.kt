package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.producers

import org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models.CurrencyProfile

interface CurrencyProfileChangeEventProducer {
    fun sendCreateEvent(currencyProfile: CurrencyProfile)

    fun sendUpdateEvent(currencyProfile: CurrencyProfile)

    fun sendDeleteEvent(currencyProfile: CurrencyProfile)
}
