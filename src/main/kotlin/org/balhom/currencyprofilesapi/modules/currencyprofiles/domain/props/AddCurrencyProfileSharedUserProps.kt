package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props

import java.util.UUID

data class AddCurrencyProfileSharedUserProps(
    val currencyProfileId: UUID,
    val userEmail: String,
    val authUserId: UUID,
)
