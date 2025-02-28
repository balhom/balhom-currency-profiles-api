package org.balhom.currencyprofilesapi.modules.currencyprofiles.domain.props

import java.util.UUID

data class RemoveCurrencyProfileSharedUserProps(
    val currencyProfileId: UUID,
    val userId: UUID,
    val authUserId: UUID,
)
