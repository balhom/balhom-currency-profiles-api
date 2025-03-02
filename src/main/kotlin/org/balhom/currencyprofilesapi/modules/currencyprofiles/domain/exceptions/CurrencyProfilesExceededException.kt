package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions

import org.balhom.currencyprofilesapi.common.data.exceptions.ApiCodeException

class CurrencyProfilesExceededException : ApiCodeException(
    errorCode = 102,
    message = "Currency profile max number reached"
)
