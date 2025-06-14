package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions

import org.balhom.currencyprofilesapi.common.data.exceptions.ApiCodeException

class OperationNotAllowedForCurrencyProfileException : ApiCodeException(
    errorCode = 105,
    message = "Operation not allowed for currency profile"
)
