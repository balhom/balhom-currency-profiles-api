package org.balhom.currencyprofilesapi.modules.currencyprofiles.infrastructure.rest.data.requests

import jakarta.validation.constraints.Email
import java.util.UUID

data class CurrencyProfileSharedUserRequest(
    @Email
    val userEmailToAdd: String?,
    val userIdToRemove: UUID?,
)
