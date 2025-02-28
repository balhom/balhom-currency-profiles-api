package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions

import org.balhom.currencyprofilesapi.common.data.exceptions.ApiCodeException

class CurrencyProfileSharedUsersExceededException : ApiCodeException(
    errorCode = 103,
    message = "Currency profile shared user max number reached"
)
