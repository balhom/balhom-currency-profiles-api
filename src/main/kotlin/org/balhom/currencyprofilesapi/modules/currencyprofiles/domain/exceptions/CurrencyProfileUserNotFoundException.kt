package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions

import org.balhom.currencyprofilesapi.common.data.exceptions.ApiCodeException

class CurrencyProfileUserNotFoundException : ApiCodeException(
    errorCode = 104,
    message = "Currency profile user not found"
)
