package org.balhom.currencyprofilesapi.common.data.exceptions

open class ApiCodeException(
    val errorCode: Int,
    message: String
) : RuntimeException(message)
