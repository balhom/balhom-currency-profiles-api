package org.balhom.currencyprofilesapi.common.data.models

import java.time.LocalDateTime

data class FileReferenceData(
    var filePath: String,
    var url: String?,
    var expiration: LocalDateTime?,
) {
    constructor() : this("", null, null)

    constructor(filePath: String) : this(filePath, null, null)
}
