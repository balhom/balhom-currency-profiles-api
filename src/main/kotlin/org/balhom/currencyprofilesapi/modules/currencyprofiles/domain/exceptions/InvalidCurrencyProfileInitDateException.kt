package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.exceptions

import org.balhom.currencyprofilesapi.common.data.exceptions.ApiCodeException

class InvalidCurrencyProfileInitDateException : ApiCodeException(
    errorCode = 101,
    message = "The init date must not be in the future"
)
