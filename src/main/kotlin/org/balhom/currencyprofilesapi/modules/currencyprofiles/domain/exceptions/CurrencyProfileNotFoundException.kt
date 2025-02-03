package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions

import org.balhom.currencyprofilesapi.common.data.exceptions.ApiCodeException

class CurrencyProfileNotFoundException : ApiCodeException(
    errorCode = 100,
    message = "Currency profile not found"
)
