package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.models

import java.util.UUID

data class CurrencyProfileSharedUser(
    var id: UUID,
    var email: String,
)
